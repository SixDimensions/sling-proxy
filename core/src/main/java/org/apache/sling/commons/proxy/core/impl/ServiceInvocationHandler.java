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
    
    /**
     * If <code>type</code> has any OSGiService annotations, verify that it 
     * actually implements these interfaces.  If both validate OK, then return
     * a new ServiceInvocationHandler.
     * 
     * If it doesn't define any OSGiService annotations, return null;
     * 
     * @param type
     * @return
     * @throws IllegalStateException - thrown when: 
     *   an OSGiService annotation is defined with a NULL service() definition
     *   an OSGiService annotation is defined with all three of: pid(), filter()
     *      and implementation() are NULL or zero length.
     *   an OSGiService annotation is defined with a service() definition of
     *      Void.class
     *   an OSGiService annotation is defined and <code>type</code> does not 
     *      implement this interface.
     */
    public static ServiceInvocationHandler newInstance(Class type) 
            throws IllegalStateException {
        ServiceInvocationHandler svcIH = null;

        Set<OSGiService> osgisvcs = getOSGiServiceAnnotations(type);

        if (osgisvcs.size() > 0) {
            validate(type, osgisvcs);
            
            verifyHasAllServiceInterfaces(type, osgisvcs);
            
            svcIH = new ServiceInvocationHandler(type);
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
     * 
     * @return Set<OSGiService> - all defined OSGiService annotations regardless
     * if they are defined directly or within @OSGiServices
     */
    private static Set<OSGiService> getOSGiServiceAnnotations(Class type) {
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
        
        return services;
    }
    
    private static void validate(Class type, Set<OSGiService> osgisvcs) {
        if (osgisvcs == null || osgisvcs.size() < 1) return;
        for (OSGiService svc : osgisvcs) {
            if (svc.service() == null) {
                String msg = "Interface " + type.getName() + " has an " +
                    "OSGiService Annotation with an empty service() definition.";
                throw new IllegalStateException(msg);
            }
            if (svc.service() == Void.class) {
                String msg = "Interface " + type.getName() + " has an " +
                    "OSGiService Annotation with a service() definition of " +
                    "Void.class.";
                throw new IllegalStateException(msg);
            }
            if (length(svc.pid()) < 1 && length(svc.filter()) < 1 && 
                    svc.implementation() == null) {
                String msg = "Interface " + type.getName() + " has OSGiService"+
                    " Annotation " + svc.service() + " but all three of " +
                    " 'pid', 'filter' and 'implementation' are NULL or empty.";
                throw new IllegalStateException(msg);
            }
        }
    }
    private static void verifyHasAllServiceInterfaces(Class type, 
            Set<OSGiService> osgisvcs) {
        if (type == null || osgisvcs == null) return;
        
        Set<Class> missing = new java.util.HashSet<Class>();
        for (OSGiService svc : osgisvcs) {
            if (! Classes.implementsInterface(type, svc.service())) {
                missing.add(svc.service());
            }
        }
        if (missing.size() > 0) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Interface ").append(type.getName()).append("is missing");
            sb.append("and 'implements' for the following interfaces:\n");
            for (Class clazz : missing) {
                sb.append(clazz.getName()).append("\n");
            }
            throw new IllegalStateException(sb.toString());
        }
    }
    
    /***************************************************************************
     * 
     * Inner Classes
     * 
     */
    private static final class FindFirstServiceOfType implements
            IAnnotationVisitor<OSGiService> {
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
