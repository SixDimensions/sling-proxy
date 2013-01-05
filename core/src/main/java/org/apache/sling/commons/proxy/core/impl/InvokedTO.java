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

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.sling.commons.proxy.api.annotations.SlingProperty;

/**
 * @author MJKelleher - Dec 23, 2012 11:28:35 PM
 *
 * proxy-poc
 *
 *
 * InvokedTO - Transfer Object Meant as a convenient way to maintain and pass a
 * method invocation around The members are not private because the class is
 * package protected and they are final.
 *
 * org.apache.sling.commons.proxy.poc.jdp.InvokedTO
 */
final class InvokedTO {

    final Object proxy;
    final Method method;
    final Object[] args;
    final String propertyName;
    final MethodType mt;

    private InvokedTO(Object proxy, Method method, Object[] args,
            String propertyName, MethodType mt) {
        this.proxy = proxy;
        this.method = method;
        this.args = args;
        this.propertyName = propertyName;
        this.mt = mt;
    }

    /**
     * ************************************************************************
     *
     *
     *
     */
    public static InvokedTO newInstance(Object proxy, Method method, Object[] args) {
        MethodType mt = MethodType.getMethodType(method);
        String property = getNameByAnnotation(method);
        if (property == null || property.length() < 1) {
            property = MethodType.getBeanName(mt, method);
            property = property.replace("_", ":");
        }
        if (property == null || property.length() < 1) {
            String msg = "Could not determine Bean Property name either from @SlingProperty annotation or the JavaBean method name.";
            throw new IllegalStateException(msg);
        }
        return new InvokedTO(proxy, method, args, property, mt);
    }

    /**
     * ************************************************************************
     *
     *
     *
     */
    public boolean isType(MethodType _mt) {
        return mt == _mt;
    }

    public boolean isGetter() {
        return MethodType.contains(new MethodType[]{MethodType.JavaBeanIs,
                    MethodType.JavaBeanGet}, mt);
    }

    public boolean isJavaBean() {
        return MethodType.contains(new MethodType[]{MethodType.JavaBeanIs,
                    MethodType.JavaBeanGet, MethodType.JavaBeanSet}, mt);
    }

    /**
     * ************************************************************************
     *
     *
     *
     */

    private static String getNameByAnnotation(Method m) {
        SlingProperty sp = m.getAnnotation(SlingProperty.class);
        if (sp == null) {
            return null;
        }

        String path = nullToZeroLength(sp.path());
        path = (path.length() > 0 ? path + "/" : path);
        String name = nullToZeroLength(sp.name());
        return path + name;
    }

    private static String nullToZeroLength(String str) {
        return (str == null ? "" : str.trim());
    }
}
