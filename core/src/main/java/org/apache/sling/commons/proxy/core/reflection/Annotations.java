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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author MJKelleher - Dec 27, 2012 5:44:47 PM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.core.reflection.Annotations
 */
public final class Annotations {

    private Annotations() {
    }

    /**
     * For the given Class 'clazz', find all of the annotations on the Class 
     * that are of the given Annotation type 'annType'.
     * 
     * @param <T> - <T extends Annotation> - Defines generic type T to be a 
     * subtype of Annotation
     * @param clazz Class - the Class to get annotations from
     * @param annType Class<T> - the annotation types to get from Class 'clazz'
     * @param visitor IAnnotationVisitor<T> - optional list of Annotation 
     * visitors that will perform some operation on each Annotation as its found.
     * Or this is completely ignored if there are no Visitors.  Each visitor 
     * is required to handle their own Exception.  This method will swallow any
     * Exceptions caused by a runaway Visitor.
     * @return Set<T> - The set of requested annotation Types assigned to the 
     * given Class
     */
    public static <T extends Annotation> Set<T> get(Class clazz,
            Class<T> annType, IAnnotationVisitor<T>... visitor) {
        java.util.HashSet<T> set = new java.util.HashSet<T>();
        
        int size = (visitor != null ? visitor.length : -1);
        
        Annotation[] anns = clazz.getAnnotations();
        if (anns != null) {
            for (Annotation ann : anns) {
                Class _annType = ann.annotationType();
                if (_annType.isAssignableFrom(annType)) {
                    T t = (T) ann;
                    set.add(t);
                    for (int ndx = 0 ; ndx < size ; ndx++) {
                        try {
                            visitor[ndx].visit(t);
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }

        return set;
    }
    
    /**
     * For the given Class'clazz' determine if it has at least one method 
     * containing an Annotation of type or subclass of type 'annType'.
     * 
     * @param <T> - <T extends Annotation> - Defines generic type T to be a 
     * subtype of Annotation
     * @param clazz Class - the Class to get annotations from
     * @param annType Class<T> - the annotation types to get from Class 'clazz'
     * @return TRUE if it does, FALSE otherwise
     */
    public static <T extends Annotation> boolean
            hasMethodAnnotation(Class clazz, Class<T> annType) {
        Method[] ma = clazz.getMethods();
        if (ma == null) return false;
        
        for (Method m : ma) {
            Annotation[] ana = m.getAnnotations();
            if (ana == null) continue;
            
            for (Annotation a : ana) {
                if (annType.isAssignableFrom(a.getClass())) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
