/*
 * Copyright 2013 Six Dimensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.sling.commons.proxy.core.impl;


import java.lang.reflect.Field;
import org.apache.sling.commons.proxy.core.reflection.Annotations;
import org.apache.sling.commons.proxy.core.reflection.IAnnotationVisitor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.apache.sling.commons.proxy.api.annotations.InjectField;
import org.apache.sling.commons.proxy.api.annotations.OSGiService;
import org.apache.sling.commons.proxy.api.annotations.OSGiServices;
import org.apache.sling.commons.proxy.core.reflection.Classes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MJKelleher - Jan 7, 2013 10:37:25 PM
 *
 * Apache Sling Proxy Core
 *
 *
 * org.apache.sling.commons.proxy.core.impl.ServiceInvocationHandler
 */
public final class ServiceInvocationHandler implements InvocationHandler {
    private static final Logger LOG = 
            LoggerFactory.getLogger(ServiceInvocationHandler.class);
    
    private final Map<Class, Object> implementations;
    private final Class proxyInterface;
    /**
     * Creates a new instance to handle Service Method (non Get/Set) method
     * invocations for Proxy Interface 'proxyInterface'
     * 
     * @param proxyInterface 
     */
    private ServiceInvocationHandler(Class proxyInterface) {
        this.proxyInterface = proxyInterface;
        this.implementations = new java.util.HashMap<Class, Object>();
    }
    
    public static ServiceInvocationHandler newInstance(Class type) {
        ServiceInvocationHandler svcIH = null;
        
        Set<Class> interfaces = Classes.getInterfaces(type);
        if (interfaces.size() > 0) {
            Set<OSGiService> osgisvcs = 
                    getOSGiServiceAnnotations(type, interfaces);

            if (osgisvcs.size() > 0) {
                Set<Class> svcIntfcs = toServiceInterfaces(osgisvcs);
                boolean atLeastOneRemoved = interfaces.removeAll(svcIntfcs);
                if (atLeastOneRemoved) {
                    svcIH = new ServiceInvocationHandler(type);
                }
            }
        }
        
        return svcIH;
    }
    
    /**
     * <p>
     * This will lazy load any of the Services it is supposed to handle.  This 
     * will:</p>
     * <ul>
     *     <li>Get the Interface from the Method argument</li>
     *     <li>Get the OSGiService Annotation for the Interface </li>
     *     <li>From the metadata on the OSGiService Annotation, "lookup" and 
     *         create an instance of this Service.  It uses the metadata in 
     *         this order to find the Implementation:
     *         <ol>
     *            <li>OSGi Service Lookup using the Interface and pid</li>
     *            <li>OSGi Service Lookup using the Interface and filter </li>
     *            <li>Use the specified <code>implementation</code></li>
     *         <ol>
     *     </li>
     *     <li>Create and cache the Service for the next invocation</li>
     * <ul>
     * @param o
     * @param method
     * @param args
     * @return
     * @throws Throwable 
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class intrfce = method.getDeclaringClass();
        Object impl = implementations.get(intrfce);
        if (impl == null) {
            impl = resolveService(proxy, intrfce);
            implementations.put(intrfce, impl);
        }
        if (impl != null) {
            Object rtn = method.invoke(impl, args);
            return rtn;
        }
        throw new UnsupportedOperationException("Method " + method.getName() + 
                " not supported.");
    }
    
    private Object resolveService(Object proxy, Class interfce) 
            throws InstantiationException, IllegalAccessException {
        FindFirstServiceOfType ar = new FindFirstServiceOfType(interfce);
        Annotations.get(proxyInterface, OSGiService.class, ar);
        OSGiService svcAnnotation = ar.getAnnotation();
        if (svcAnnotation == null) {
            String msg = "Could not find OSGiService annotation for Interface "+
                    interfce.getName() + ".";
            throw new IllegalStateException(msg);
        }
        
        Object returnService = null;
        if (isDefinedAsOSGiService(svcAnnotation)) {
            //** @TODO  UNCOMPLETED
            throw new UnsupportedOperationException("NOT YET SUPPORTED");
        } else if (svcAnnotation.implementation() != null) {
            returnService =  Classes.newInstance(svcAnnotation.implementation(), 
                    svcAnnotation.service());
        }
        if (returnService != null) {
            injectMember(returnService, proxy);
            return returnService;
        }
        String msg = "Interface " + interfce.getName() + " has an OSGiService "+
                "Annotation, but had neither an OSGi Service or a Static " +
                "Implementation defined for it.";
        throw new UnsupportedOperationException(msg);
    }
    
    private void injectMember(Object service, Object proxy) 
            throws IllegalAccessException {
        if (proxy == null) return;
        
        Field[] fa = service.getClass().getDeclaredFields();
        int size = (fa != null ? fa.length : -1);
        if (size < 1) return;
        
        for (Field f : fa) {
            InjectField ann = f.getAnnotation(InjectField.class);
            if (ann == null) continue;
            
            if (! f.getType().isAssignableFrom(proxyInterface)) continue;
            
            Boolean isAccessible = null;
            if (! f.isAccessible()) {
                isAccessible = Boolean.FALSE;
                f.setAccessible(true);
            }
            f.set(service, proxy);
            if (isAccessible != null) {
                f.setAccessible(false);
            }
        }
    }
    /***************************************************************************
     * 
     * Static Utility Methods
     * 
     */
    
    private static boolean isDefinedAsOSGiService(OSGiService annot) {
        return length(annot.pid()) > 0 || length(annot.filter()) > 0;
    }
    
    private static int length(String str) {
        return (str != null ? str.trim().length() : -1);
    }
    
    /**
     * This takes care of both: OSGiServices and OSGiService annotations
     *
     * @param type Class - the JDP Interface to be implemented by the Proxy
     * @param interfaces Set<Class> - the Interfaces defined on class 'type' - 
     * this is used to validate that the identified OSGiService annotations 
     * found on 
     * <code>type</code> actually has a corresponding Interface in the Set 
     * <code>interfaces<code>
     * @return Set<OSGiService> - all defined OSGiService annotations regardless
     * if they are defined directly or within @OSGiServices
     */
    private static Set<OSGiService> getOSGiServiceAnnotations(Class type, 
            Set<Class> interfaces) {
        Set<OSGiService> services = new java.util.HashSet<OSGiService>();

        Set<OSGiServices> anns1 = Annotations.get(type, OSGiServices.class);
        if (anns1.size() > 0) {
            for (OSGiServices svcs : anns1) {
                OSGiService[] sa = svcs.value();
                if (sa != null) {
                    for (OSGiService s : sa) {
                        services.add(s);
                    }
                }
            }
        }
        Set<OSGiService> anns2 = Annotations.get(type, OSGiService.class);
        if (anns2 != null && anns2.size() > 0) {
            services.addAll(anns2);
        }
        
        return returnValidatedServices(type, interfaces, services);
    }
    
    private static Set<OSGiService> returnValidatedServices(Class type, 
            Set<Class> interfaces, Set<OSGiService> services) {
        Set<OSGiService> rtn = new java.util.HashSet<OSGiService>();
        for (OSGiService s : services) {
            if (s.service() == null) {
                String msg = "Interface {} was annotated with OSGiService, " +
                        "but it's Service value was NULL";
                LOG.warn(msg, type.getName());
                continue;
            }
            if (s.service() == Void.class) {
                String msg = "Interface {} was annotated with OSGiService, " +
                        "but it's Service value was {}";
                LOG.warn(msg, type.getName(), Void.class.getName());
                continue;
            }
            if (interfaces.contains(s.service())) {
                rtn.add(s);
            } else {
                String msg = "Interface {} was annotated with OSGiService, " +
                        "but it did not extend interface {}";
                LOG.warn(msg, type.getName(), s.service().getName());
            }
        }

        return rtn;
    }
    
    private static Set<Class> toServiceInterfaces(Set<OSGiService> set) {
        Set<Class> rtn = new java.util.HashSet<Class>();
        if (set != null && set.size() > 0) {
            for (OSGiService svc : set) {
                rtn.add(svc.service());
            }
        }
        return rtn;
    }
    
    /***************************************************************************
     * 
     * Inner Classes
     * 
     */
    private static final class FindFirstServiceOfType implements IAnnotationVisitor<OSGiService> {
        private final Class serviceInterface;
        private OSGiService annotation;
        
        FindFirstServiceOfType(Class serviceInterface) {
            this.serviceInterface = serviceInterface;
        }
        public OSGiService getAnnotation() {
            return annotation;
        }
        public void visit(OSGiService t) {
            if (annotation == null) {
                if (t.service() == serviceInterface) {
                    annotation = t;
                }
            }
        }
    }
}
