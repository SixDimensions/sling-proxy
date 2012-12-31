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
package org.apache.sling.commons.jdp.impl;

import java.lang.reflect.Method;

import org.apache.sling.commons.jdp.api.annotations.SlingProperty;

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
    final String beanName;
    final MethodType mt;

    private InvokedTO(Object proxy, Method method, Object[] args,
            String beanName, MethodType mt) {
        this.proxy = proxy;
        this.method = method;
        this.args = args;
        this.beanName = beanName;
        this.mt = mt;
    }

    /**
     * ************************************************************************
     *
     *
     *
     */
    public static InvokedTO newInstance(Object proxy, Method method, Object[] args) {
        MethodType mt = getMethodType(method);
        String bean = getBeanNameByAnnotation(method);
        if (bean == null) {
            bean = getBeanNameByMethodName(method, mt);
        }
        return new InvokedTO(proxy, method, args, bean, mt);
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
        return isType(new MethodType[]{MethodType.JavaBeanIs,
                    MethodType.JavaBeanGet});
    }

    public boolean isJavaBean() {
        return isType(new MethodType[]{MethodType.JavaBeanIs,
                    MethodType.JavaBeanGet, MethodType.JavaBeanSet});
    }

    private boolean isType(MethodType[] mta) {
        for (MethodType _mt : mta) {
            if (mt == _mt) {
                return true;
            }
        }
        return false;
    }

    /**
     * ************************************************************************
     *
     *
     *
     */
    private static MethodType getMethodType(Method method) {
        String name = method.getName();
        MethodType mt = MethodType.Unknown;
        if (name.startsWith("is")) {
            mt = MethodType.JavaBeanIs;
        } else if (name.startsWith("get")) {
            mt = MethodType.JavaBeanGet;
        } else if (name.startsWith("set")) {
            mt = MethodType.JavaBeanSet;
        } else if (name.equals("toString")) {
            mt = MethodType.ToString;
        } else if (name.equals("hashCode")) {
            mt = MethodType.HashCode;
        } else if (name.equals("equals")) {
            mt = MethodType.Equals;
        }
        return mt;
    }

    private static String getBeanNameByMethodName(Method method, MethodType mt) {
        String name = method.getName();
        int ndx = -1;
        if (mt == MethodType.JavaBeanIs) {
            ndx = 2;
        } else if (mt == MethodType.JavaBeanGet) {
            ndx = 3;
        } else if (mt == MethodType.JavaBeanSet) {
            ndx = 3;
        }
        String bean = null;
        if (ndx > 0) {
            bean = name.substring(ndx);
            bean = bean.substring(0, 1).toLowerCase() + bean.substring(1);
            bean = bean.replace("_", ":");
        }
        return bean;
    }

    private static String getBeanNameByAnnotation(Method m) {
        SlingProperty sp = m.getAnnotation(SlingProperty.class);
        return (sp != null ? sp.path() : null);
    }
}
