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
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.api.IJDPFactory;
import org.apache.sling.commons.proxy.api.SlingProxy;
import org.apache.sling.commons.proxy.api.annotations.SlingProperty;
import org.apache.sling.commons.proxy.core.reflection.Annotations;
/**
 * @author MJKelleher - Dec 24, 2012 9:53:39 AM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.jdp.DefaultJDPImpl
 */
@Service(value=IJDPFactory.class)
@Component(description="Creates new SlingProxy instances",
        immediate=true)
public final class DefaultJDPFactoryImpl implements IJDPFactory {
    
    /**
     * Creates new SlingProxy instances of the provided type <code>type</code>
     * using the <code>r</code> as the backing Sling Resource for the JavaBean
     * accessor methods on <code>type</code>
     * 
     * The following criteria must be met when invoking this method:
     * <ol>
     *    <li><code>r</code> must not be null</li>
     *    <li><code>type</code> must not be null</li>
     *    <li><code>type</code> must be an Interface</li>
     *    <li>Interface <code>type</code> must have at least one 
     *        <code>@SlingProperty</code> Annotation
     *    </li>
     *    <li></li>
     * <ol>
     * 
     * @param <T>  extends SlingProxy
     * @param r Resource - the backing Resource
     * @param type Class - the interface that extends SlingProxy that is the
     * Interface we are to create a new Proxy instance of
     * @return the new proxy instance of type <code>type</code>
     */
    public <T extends SlingProxy> T newInstance(Resource r, Class<T> type) {
        validateIsInstantiable(r, type);
        
        InvocationHandler ih = new ResourceInvocationHandler(r);
        T rtn = (T) Proxy.newProxyInstance(type.getClassLoader(), 
                new Class[] { type } , ih);
        return rtn;
    }
    
    private static <T> void validateIsInstantiable(Resource r, Class<T> type) {
        if (r == null) {
            String msg = "The backing Resource cannot be NULL.";
            throw new NullPointerException(msg);
        }
        if (type == null) {
            String msg = "The Provided SlingBean Interface cannot be NULL.";
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
    
}
