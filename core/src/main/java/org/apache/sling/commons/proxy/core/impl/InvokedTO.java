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
    public static final InvokedTO UNKNOWN = new InvokedTO(null, null, null, null, null, MethodType.Unknown);

    final Object proxy;
    final Method method;
    final Object[] args;
    final String path;
    final String name;
    final String propertyName;
    final MethodType mt;

    private InvokedTO(Object proxy, Method method, Object[] args,
            String path, String name, MethodType mt) {
        this.proxy = proxy;
        this.method = method;
        this.args = args;
        this.path = path;
        this.name = name;
        this.propertyName = (path != null ? path + "/" : "") + name;
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
        if (mt == MethodType.Unknown) {
            /**
             * An unknown MethodType is likely to be a ServiceInterface Method
             * Invocation.  The processing below is not valid for 
             * ServiceInterface Methods.
             */
            return UNKNOWN;
        }
        
        String property = getNameByAnnotation(method);
        if (property == null || property.length() < 1) {
            property = MethodType.getBeanName(mt, method);
            property = property.replace("_", ":");
        }
        if (property == null || property.length() < 1) {
            String msg = "Could not determine Bean Property name either from @SlingProperty annotation or the JavaBean method name.";
            throw new IllegalStateException(msg);
        }
        int ndx = property.lastIndexOf("/");
        String path = (ndx > -1 ? property.substring(0, ndx) : null);
        String name = (ndx > -1 ? property.substring(ndx + 1) : property);
        return new InvokedTO(proxy, method, args, path, name, mt);
    }

    /**
     * ************************************************************************
     *
     *
     *
     */
    /**
     * Determines if the Property represented by this method Invocation is an
     * absolute path reference, starts with a /.
     * @return TRUE if the Property starts with /, FALSE otherwise
     */
    public boolean isAbsolute() {
        return path != null && path.startsWith("/");
    }
    /**
     * Determines if the Property represented by this method Invocation is a
     * relative path reference, that is a descendant of the backing Resource
     * @return TRUE if the Property contains / but does not start with /, FALSE
     * otherwise
     */
    public boolean isRelative() {
        return path != null && ! isAbsolute() && path.contains("/");
    }
    /**
     * Determines if the Property represented by this method Invocation is a
     * direct reference, that is stored immediately within the backing Resource.
     * @return TRUE if the Property is stored directly within the Resource, 
     * FALSE otherwise
     */
    public boolean isDirect() {
        return ! isRelative();
    }
    
    /**
     * Determines if the current Invocation is of type <code>_mt</code>
     * @param _mt MethodType
     * @return TRUE if it is, FALSE otherwise
     */
    public boolean isType(MethodType _mt) {
        return mt == _mt;
    }
    
    /**
     * Determines if the current Invocation is a 'Getter' method - that is a 
     * method that starts with 'get' or 'is', has no arguments, and returns a
     * value.
     * @return TRUE if it is a 'get' or 'is' method, FALSE otherwise
     */
    public boolean isGetter() {
        return MethodType.contains(new MethodType[]{MethodType.JavaBeanIs,
                    MethodType.JavaBeanGet}, mt);
    }
    
    /**
     * Determines if the current Invocation is named in the JavaBeans style, '
     * that is 'get', 'is' or 'set'.
     * @return 
     */
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
