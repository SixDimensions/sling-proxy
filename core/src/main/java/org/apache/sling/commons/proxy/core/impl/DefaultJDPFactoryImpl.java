/*
 * Copyright 2012 Six Dimensions.
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Set;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.api.IJDPFactory;
import org.apache.sling.commons.proxy.api.annotations.OSGiService;
import org.apache.sling.commons.proxy.api.annotations.OSGiServices;
import com.apache.sling.commons.proxy.core.reflection.Annotations;
import com.apache.sling.commons.proxy.core.reflection.Classes;
import org.apache.sling.commons.proxy.api.annotations.SlingProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MJKelleher - Dec 24, 2012 9:53:39 AM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.jdp.DefaultJDPImpl
 */
public final class DefaultJDPFactoryImpl implements IJDPFactory {

    private static final Logger LOG = 
            LoggerFactory.getLogger(DefaultJDPFactoryImpl.class);

    public <T> T newInstance(Resource r, Class<T> type) {
        validateIsInstantiable(r, type);
        
        InvocationHandler ih = new ResourceInvocationHandler(r);
        T rtn = (T) Proxy.newProxyInstance(type.getClassLoader(), 
                new Class[]{type}, ih);

        Set<Class> interfaces = Classes.getInterfaces(type);
        if (interfaces.size() > 0) {
            Set<OSGiService> osgisvcs = 
                    getOSGiServiceAnnotations(type, interfaces);

            if (osgisvcs.size() > 0) {
                Set<Class> svcIntfcs = toServiceInterfaces(osgisvcs);
                boolean atLeastOneRemoved = interfaces.removeAll(svcIntfcs);
            }
        }

        return rtn;
    }
    
    private static <T> void validateIsInstantiable(Resource r, Class<T> type) {
        if (r == null) {
            String msg = "The Resource cannot be NULL.";
            throw new NullPointerException(msg);
        }
        if (type == null) {
            String msg = "The Provided Interface cannot be NULL.";
            throw new NullPointerException(msg);
        }
        if (!type.isInterface()) {
            String msg = "Class " + type.getName() + " must be an Interface.";
            throw new UnsupportedOperationException(msg);
        }
        if (! Annotations.hasMethodAnnotation(type, SlingProperty.class)) {
            String msg = "Interface " + type.getName() + " must have at least "+
                    "one Method with a @SlingProperty annotation.";
            throw new UnsupportedOperationException(msg);
        }
    }

    /**
     * This takes care of both: OSGiServices and OSGiService annotations
     *
     * @param type Class - the JDP Interface to be implemented by the Proxy
     * @param interfaces - the Interfaces defined on class 'type'
     * @return Set<OSGiService> - all defined OSGiService annotations regardless
     * if they are defined directly or within @OSGiServices
     */
    private static final Set<OSGiService> getOSGiServiceAnnotations(Class type, 
            Set<Class> interfaces) {
        Set<OSGiService> services = new java.util.HashSet<OSGiService>();

        Set<OSGiServices> anns = Annotations.get(type, OSGiServices.class);
        if (anns.size() < 1) {
            services.addAll(Annotations.get(type, OSGiService.class));
        } else {
            for (OSGiServices svcs : anns) {
                OSGiService[] sa = svcs.value();
                if (sa != null) {
                    for (OSGiService s : sa) {
                        services.add(s);
                    }
                }
            }
        }

        Set<OSGiService> rtn = new java.util.HashSet<OSGiService>();
        for (OSGiService s : services) {
            if (s.service() == null) {
                String msg = "Interface {} was annotated with OSGiService, " +
                        "but it's Service value was NULL";
                LOG.warn(msg, type.getName());
                continue;
            }
            if (s.service() != Void.class) {
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

    private static final Set<Class> toServiceInterfaces(Set<OSGiService> set) {
        Set<Class> rtn = new java.util.HashSet<Class>();
        if (set != null && set.size() > 0) {
            for (OSGiService svc : set) {
                rtn.add(svc.service());
            }
        }
        return rtn;
    }
}
