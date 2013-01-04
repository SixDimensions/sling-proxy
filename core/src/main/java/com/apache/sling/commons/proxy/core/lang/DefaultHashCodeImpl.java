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
package com.apache.sling.commons.proxy.core.lang;

import com.apache.sling.commons.proxy.core.lang.IHashCode;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import com.apache.sling.commons.proxy.core.reflection.Classes;

/**
 * @author MJKelleher - Dec 26, 2012 1:46:08 PM
 *
 * proxy-poc
 *
 * Generates a HashCode for the provided Object based on: If the Object is not a
 * Proxy Object, the class name of the Object the implemented interfaces class
 * name in ascending Order the Members hashCode value, evaluated in the members
 * class name ascending order. If the value in NULL, a zero value is used.
 *
 * Any exceptions thrown while accessing the Objects member values will result
 * in a IllegalStateException
 *
 * org.apache.sling.commons.reflection.DefaultHashCodeImpl
 */
public final class DefaultHashCodeImpl implements IHashCode {

    /*
     * (non-Javadoc) @see
     * org.apache.sling.commons.reflection.IHashCode#hashCode(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    public int hashCode(Object obj) throws IllegalStateException {
        List<Integer> list = new java.util.ArrayList<Integer>();

        Class c = obj.getClass();

        if (Proxy.isProxyClass(c)) {
            list.add(Proxy.class.hashCode());
            handleInterfaces(c, list);
            InvocationHandler ih = Proxy.getInvocationHandler(obj);
            list.add(ih.hashCode());
        } else {
            list.add(c.hashCode());
            handleInterfaces(c, list);
            handleFields(obj, list);
        }

        return calculate(list);
    }

    @SuppressWarnings("rawtypes")
    private void handleInterfaces(Class c, List<Integer> list) {

        Class[] ifcs = c.getInterfaces();
        if (ifcs != null) {
            Arrays.sort(ifcs, new ClassNameComparator());
            for (Class ifc : ifcs) {
                list.add(ifc.hashCode());
            }
        }
    }

    private void handleFields(Object obj, List<Integer> list) {
        List<Field> fields = Classes.getFields(obj);
        if (fields != null && fields.size() > 0) {
            Collections.sort(fields, new FieldComparator());
            for (Field f : fields) {
                try {
                    boolean unset = false;
                    if (!f.isAccessible()) {
                        unset = true;
                        f.setAccessible(true);
                    }
                    Object member = f.get(obj);
                    if (unset) {
                        f.setAccessible(false);
                    }
                    list.add((member == null ? 0 : member.hashCode()));
                } catch (Exception ex) {
                    throw new IllegalStateException(ex);
                }
            }
        }
    }

    /**
     * The first element in the List will always be the primary Class
     *
     * @param list
     * @return
     */
    private int calculate(List<Integer> list) {
        Iterator<Integer> i = list.listIterator();

        /**
         * We start at PrimaryNumber index 2 because: 0 - is only a placeholder
         * 1 - is the value 2 - which we never want to use
         */
        int hash = i.next(),
                primeNdx = 2;

        PrimeNumber pn = PrimeNumber.getInstance();
        for (; i.hasNext();) {
            hash = hash * pn.get(primeNdx) + i.next();
        }

        return hash;
    }

    /**
     * This will return NULL if both values are not null. Otherwise it will
     * return: 0 - if both are NULL or both have the same Object identity -1 -
     * if 'o1' is NULL but 'o2' is not 1 - if 'o1' is not null but 'o2' is
     *
     * @param o1 Object
     * @param o2 Object
     * @return see above
     */
    private static final Integer compareForNull(Object o1, Object o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        return null;
    }

    private static final class FieldComparator implements Comparator<Field> {

        public int compare(Field o1, Field o2) {
            Integer rtn = compareForNull(o1, o2);
            if (rtn != null) {
                return rtn;
            }

            return o1.getType().getName().compareTo(o2.getType().getName());
        }
    }

    private static final class ClassNameComparator implements Comparator<Class> {

        public int compare(Class o1, Class o2) {
            Integer rtn = compareForNull(o1, o2);
            if (rtn != null) {
                return rtn;
            }

            return o1.getName().compareTo(o2.getName());
        }
    }
}
