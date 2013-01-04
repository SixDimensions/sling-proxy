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
package com.apache.sling.commons.proxy.core.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Set;

/**
 * @author MJKelleher - Dec 25, 2012 2:59:42 PM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.api.reflection.Classes
 */
public final class Classes {

    private Classes() {
    }

    /**
     * Convenience method for returning as a Set the Interfaces the given class
     * extends or implements
     *
     * @param c Class
     * @return Set<Class> - guaranteed to be non Null, but may be empty
     */
    public static Set<Class> getInterfaces(Class c) {
        Class[] ca = c.getInterfaces();
        int size = (ca == null ? 0 : ca.length);
        Set<Class> set = new java.util.HashSet<Class>(size);
        for (Class clazz : ca) {
            set.add(clazz);
        }
        return set;
    }

    /**
     * Find the first Class that contains the given Method
     *
     * @param ca Class[]
     * @param m Method
     * @return Class - the class containing the Method or NULL if not found
     */
    @SuppressWarnings("rawtypes")
    public static Class getClass(Class[] ca, Method m) {
        int size = size(ca);

        if (size > 0) {
            for (Class c : ca) {
                if (contains(c, m)) {
                    return c;
                }
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static boolean contains(Class c, Method m) {
        try {
            Method m2 = c.getMethod(m.getName(), m.getParameterTypes());
            return (m == m2 || m.getReturnType() == m2.getReturnType());
        } catch (NoSuchMethodException ex) {
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    public static List<Field> getFields(Object obj) {
        java.util.ArrayList<Field> list = new java.util.ArrayList<Field>();

        Class c = obj.getClass();
        for (; c != null && c != Object.class;) {
            for (Field f : c.getDeclaredFields()) {
                if (!f.isSynthetic()) {
                    list.add(f);
                }
            }
            c = c.getSuperclass();
        }

        list.trimToSize();
        return list;
    }

    /**
     * To have the same set of interfaces the two Objects must be one of the
     * following: > be equal in identity to one another, or both NULL > have 0
     * interfaces and either be Proxy Classes or be the same Type > must have
     * the same number of interfaces, and the same interfaces
     *
     * @param o1
     * @param o2
     * @return
     */
    public static boolean haveSameInterfaces(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }

        Class[] ca1 = o1.getClass().getInterfaces();
        Class[] ca2 = o2.getClass().getInterfaces();
        int size1 = size(ca1);
        if (size1 != size(ca2)) {
            return false;
        }
        if (size1 < 1) {
            if (Proxy.isProxyClass(o1.getClass()) && Proxy.isProxyClass(o2.getClass())) {
                return true;
            } else if (o1.getClass() == o2.getClass()) {
                return true;
            }
            return false;
        }

        Set<Class> set = new java.util.HashSet<Class>(size1);
        for (Class c : ca1) {
            set.add(c);
        }
        for (Class c : ca2) {
            set.remove(c);
        }
        return set.size() < 1;
    }

    private static final int size(Class[] ca) {
        return (ca != null ? ca.length : -1);
    }
}
