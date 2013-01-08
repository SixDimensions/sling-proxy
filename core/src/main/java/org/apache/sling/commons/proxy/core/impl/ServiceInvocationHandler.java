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


import com.apache.sling.commons.proxy.core.reflection.Annotations;
import com.apache.sling.commons.proxy.core.reflection.IAnnotationVisitor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.apache.sling.commons.proxy.api.annotations.OSGiService;
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
    public ServiceInvocationHandler(Class proxyInterface) {
        this.proxyInterface = proxyInterface;
        this.implementations = new java.util.HashMap<Class, Object>();
    }

    public Object invoke(Object o, Method method, Object[] os) throws Throwable {
        Class intrfce = method.getDeclaringClass();
        Object impl = implementations.get(intrfce);
        if (impl == null) {
            /**
             * @TODO: UNCOMPLETED!!
             */
        }
        throw new UnsupportedOperationException("Method " + method.getName() + 
                " not supported.");
    }
    
    private Object resolveService(Class interfce) {
        AnnotationResolver ar = new AnnotationResolver(interfce);
        Annotations.get(proxyInterface, OSGiService.class, ar);
        if (ar.getAnnotation() == null) {
            String msg = "Could not find OSGiService annotation for Interface "+
                    interfce.getName() + ".";
            throw new IllegalStateException(msg);
        }
        
        /**
         * @TODO: UNCOMPLETED!!
         */
        return null;
    }
    
    private static final class AnnotationResolver implements IAnnotationVisitor<OSGiService> {
        private final Class serviceInterface;
        private OSGiService annotation;
        
        AnnotationResolver(Class serviceInterface) {
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
