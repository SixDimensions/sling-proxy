/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.commons.proxy.impl.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Set;

/**
 * Methods for simplifying reflection class calls.
 */
public final class Classes {

	/**
	 * Get all of the member fields for Object 'obj' - including private,
	 * protected, default, and Public. This does not include inherited fields.
	 * This will not include Synthetic fields either.
	 * 
	 * @param obj
	 *            Object
	 * @return List<Field> the list of Fields from an Object
	 */
	public static List<Field> getFields(Object obj) {
		java.util.ArrayList<Field> list = new java.util.ArrayList<Field>();

		Class<?> c = obj.getClass();
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

		Class<?>[] ca1 = o1.getClass().getInterfaces();
		Class<?>[] ca2 = o2.getClass().getInterfaces();
		int size1 = size(ca1);
		if (size1 != size(ca2)) {
			return false;
		}
		if (size1 < 1) {
			if (Proxy.isProxyClass(o1.getClass())
					&& Proxy.isProxyClass(o2.getClass())) {
				return true;
			} else if (o1.getClass() == o2.getClass()) {
				return true;
			}
			return false;
		}

		Set<Class<?>> set = new java.util.HashSet<Class<?>>(size1);
		for (Class<?> c : ca1) {
			set.add(c);
		}
		for (Class<?> c : ca2) {
			set.remove(c);
		}
		return set.size() < 1;
	}


	/**
	 * Checks the size of the class arraym return the size or -1 if the array is
	 * null.
	 * 
	 * @param ca
	 *            the array to check
	 * @return the size of the array
	 */
	private static final int size(Class<?>[] ca) {
		return (ca != null ? ca.length : -1);
	}
}
