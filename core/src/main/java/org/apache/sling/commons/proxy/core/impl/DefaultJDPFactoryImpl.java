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
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.api.IJDPFactory;
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
public final class DefaultJDPFactoryImpl implements IJDPFactory {

    public <T> T newInstance(Resource r, Class<T> type) {
        validateIsInstantiable(r, type);
        
        InvocationHandler svcIH = ServiceInvocationHandler.newInstance(type);
        
        T rtn = ResourceInvocationHandler.newInstance(type, r, svcIH);

        return rtn;
    }
    
    private static <T> void validateIsInstantiable(Resource r, Class<T> type) {
        if (r == null) {
            String msg = "The Resource cannot be NULL.";
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
