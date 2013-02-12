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
package org.apache.sling.commons.proxy.impl.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Methods for simplifying reflection annotation calls.
 */
public final class Annotations {

	/**
	 * For the given Class'clazz' determine if it has at least one method
	 * containing an Annotation of type or subclass of type 'annType'.
	 * 
	 * @param <T>
	 *            - <T extends Annotation> - Defines generic type T to be a
	 *            subtype of Annotation
	 * @param clazz
	 *            Class - the Class to get annotations from
	 * @param annType
	 *            Class<T> - the annotation types to get from Class 'clazz'
	 * @return TRUE if it does, FALSE otherwise
	 */
	public static <T extends Annotation> boolean hasMethodAnnotation(
			Class<?> clazz, Class<T> annType) {
		Method[] ma = clazz.getMethods();
		if (ma == null)
			return false;

		for (Method m : ma) {
			Annotation[] ana = m.getAnnotations();
			if (ana == null)
				continue;

			for (Annotation a : ana) {
				if (annType.isAssignableFrom(a.getClass())) {
					return true;
				}
			}
		}

		return false;
	}
}
