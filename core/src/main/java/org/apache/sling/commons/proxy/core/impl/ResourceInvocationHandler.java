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
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.core.lang.DefaultHashCodeImpl;
import org.apache.sling.commons.proxy.core.lang.GetMethodToStringImpl;
import org.apache.sling.commons.proxy.core.lang.IEquals;
import org.apache.sling.commons.proxy.core.lang.IHashCode;
import org.apache.sling.commons.proxy.core.lang.PrimeNumber;
import org.apache.sling.api.resource.ValueMap;

/**
 * @author MJKelleher - Dec 23, 2012 11:27:36 PM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.jdp.ResourceInvocationHandler
 */
final class ResourceInvocationHandler implements InvocationHandler {
    
    /**
     * The backing Sling Resource
     */
    private final Resource r;
    /**
     * Handles Service Method Invocation - non GET/SET/IS Methods
     */
    private final InvocationHandler serviceInvocationHandler;
    /**
     * This caches all 'get' or 'is' method's return values.  Calling 'set' will
     * clear that properties cached value.
     */
    private final Map<String, Object> cache;
    
    /**
     * This is almost the same Object as <code>proxy</code> passed into 
     * 'invoke(..)', except this one will not have a ServiceInvocationHandler
     * instance.  This will prevent recursive Service Method calls to itself.
     */
    private final Object injectablePxy;
    
    
    /**
     * Create a new ResourceInvocationHandler allowing invocation of all Methods
     * that this InvocationHandler represents
     *
     * @param r Resource - the
     */
    @SuppressWarnings("rawtypes")
    private ResourceInvocationHandler(Resource r, 
            InvocationHandler serviceInvocationHandler,
            Object injectableProxy) {
        this.r = r;
        this.serviceInvocationHandler = serviceInvocationHandler;
        this.cache = new java.util.HashMap<String, Object>();
        this.injectablePxy = injectableProxy;
    }
    
    public static <T> T newInstance(Class<T> type, Resource r, 
            InvocationHandler serviceIH) {
        T injectable = null;
        if (serviceIH != null) {
            InvocationHandler injectableIH = new ResourceInvocationHandler(r, null, null);
            injectable = (T) Proxy.newProxyInstance(type.getClassLoader(), 
                new Class[] { type } , injectableIH);
        }
        
        InvocationHandler ih = new ResourceInvocationHandler(r, serviceIH, injectable);
        T rtn = (T) Proxy.newProxyInstance(type.getClassLoader(), 
                new Class[] { type } , ih);
        return rtn;
    }

    /**
     * ************************************************************************
     *
     *
     */
    /*
     * (non-Javadoc) @see
     * java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     * java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        
        InvokedTO to = InvokedTO.newInstance(proxy, method, args);
        if (to.isGetter()) {
            return (handleGet(to));
        } else if (to.isType(MethodType.JavaBeanSet)) {
            throw new UnsupportedOperationException("Method not yet implemented.");
        } else if (to.isType(MethodType.ToString)) {
            return new GetMethodToStringImpl().toString(proxy);
        } else if (to.isType(MethodType.HashCode)) {
            IHashCode hc = new DefaultHashCodeImpl();
            return hc.hashCode(proxy);
        } else if (to.isType(MethodType.Equals)) {
            if (args == null || args.length != 1) {
                String msg = "Method 'equals' requires exactly 1 argument.";
                throw new IllegalArgumentException(msg);
            }
            IEquals ieq = new JDPEqualsImpl();
            return ieq.equals(proxy, args[0]);
        } else if (InvokedTO.UNKNOWN == to) {
            if (serviceInvocationHandler != null) {
                Object pxyObj = (injectablePxy != null ? injectablePxy : proxy);
                return serviceInvocationHandler.invoke(pxyObj, method, args);
            }
        }
        throw new NoSuchMethodException("Method " + method.getName() + " DNE");
    }

    @Override
    public int hashCode() {
        int hashCode = ResourceInvocationHandler.class.hashCode() * PrimeNumber.getInstance().get(2) + r.getPath().hashCode();
        return hashCode;
    }

    /**
     * ************************************************************************
     *
     *
     */
    final String getResourcePath() {
        return r.getPath();
    }

    /**
     * ************************************************************************
     *
     *
     */
    /**
     * @TODO:  refactor to also cache the ValueMap for a given path?
     * 
     * @param to
     * @return
     * @throws Throwable 
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object handleGet(InvokedTO to) throws Throwable {
        Object objReturn;
        if (cache.containsKey(to.propertyName)) {
            objReturn = cache.get(to.propertyName);
        } else {
            ValueMap vm;
            if (to.path == null) {
                vm = r.adaptTo(ValueMap.class);
            } else {
                Resource rsrc;
                if (to.isAbsolute()) {
                     rsrc = r.getResourceResolver().getResource(to.path);
                } else {
                    rsrc = r.getResourceResolver().getResource(r, to.path);
                }
                vm = rsrc.adaptTo(ValueMap.class);
            }
            objReturn = vm.get(to.name, to.method.getReturnType());
            cache.put(to.propertyName, objReturn);
        }
        return objReturn;
    }
}
