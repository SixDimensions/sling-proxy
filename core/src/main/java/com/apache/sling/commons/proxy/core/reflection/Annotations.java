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

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author MJKelleher - Dec 27, 2012 5:44:47 PM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.reflection.Annotations
 */
public final class Annotations {

    private Annotations() {
    }

    /**
     * 
     * @param <T> - <T extends Annotation> - Defines generic type T to be a 
     * subtype of Annotation
     * @param clazz Class - the Class to get annotations from
     * @param annType Class<T> - the annotation types to get from Class 'clazz'
     * @return Set<T> - The set of requested annotation Types assigned to the 
     * given Class
     */
    public static <T extends Annotation> Set<T> get(Class clazz,
            Class<T> annType) {
        java.util.HashSet<T> set = new java.util.HashSet<T>();

        Annotation[] anns = clazz.getAnnotations();
        if (anns != null) {
            for (Annotation ann : anns) {
                if (ann.getClass().isAssignableFrom(annType)) {
                    set.add((T) ann);
                }
            }
        }

        return set;
    }
}
