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
package org.apache.sling.commons.proxy.core.reflection;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author MJKelleher - Dec 24, 2012 4:06:16 PM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.api.reflection.Methods
 */
public final class Methods {

    private Methods() {
    }

    public static List<Method> getterMethods(Object o) {
        List<Method> list = getMethods(o, "^(get|is)\\w*$", true, 0);
        return list;
    }

    /**
     *
     * @param o Object - the target Object
     * @param nameRegex String - the regex to use for matching method names. If
     * left NULL, this will use: ^.*$
     * @param hasReturn boolean - switch to check wether or not the method has a
     * return value. TRUE it will be checked, false return type is ignored.
     * @param numberOfParameters int - The number of arguments the method must
     * have. A negative value will ignore arguments, otherwise the number of
     * arguments is checked.
     * @return List<Method>
     */
    public static List<Method> getMethods(Object o, String nameRegex,
            boolean hasReturn, int numberOfParameters) {
        nameRegex = (nameRegex == null || nameRegex.trim().length() < 1 ? "^.*$" : nameRegex);

        List<Method> list = new java.util.ArrayList<Method>();
        Method[] ma = o.getClass().getMethods();

        if (ma != null && ma.length > 0) {
            for (Method m : ma) {
                boolean checkReturnOK = true;
                if (hasReturn) {
                    Class c = m.getReturnType();
                    checkReturnOK = (c != null && c != Void.class);
                }
                if (checkReturnOK) {
                    Class[] ca = m.getParameterTypes();
                    int size = (ca == null ? 0 : ca.length);

                    if (numberOfParameters < 0 || size == numberOfParameters) {
                        String name = m.getName();
                        if (name.matches(nameRegex)) {
                            list.add(m);
                        }
                    }
                }
            }
        }

        return list;
    }
}
