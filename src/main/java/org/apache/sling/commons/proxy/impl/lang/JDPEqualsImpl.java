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
package org.apache.sling.commons.proxy.impl.lang;

import java.lang.reflect.Proxy;

import org.apache.sling.commons.proxy.impl.ResourceInvocationHandler;
import org.apache.sling.commons.proxy.impl.reflection.Classes;

/**
 * Allows for testing the equality of the Sling Proxy instance.
 */
public class JDPEqualsImpl {

	/**
	 * Test if the Sling Proxy object is equal to another object.
	 * 
	 * @param o1
	 *            the first object
	 * @param o2
	 *            the second object
	 * @return whether or not the objects are equal
	 */
	public boolean equals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		if (!areProxies(o1, o2)) {
			return false;
		}

		try {
			ResourceInvocationHandler rih1 = (ResourceInvocationHandler) Proxy
					.getInvocationHandler(o1);
			ResourceInvocationHandler rih2 = (ResourceInvocationHandler) Proxy
					.getInvocationHandler(o2);
			if (Classes.haveSameInterfaces(o1, o2)) {
				return stringEquals(rih1.getResourcePath(),
						rih2.getResourcePath());
			}
		} catch (ClassCastException ex) {
		}
		return false;
	}

	/**
	 * Compares the two strings for equality, either string being null results
	 * in inequality.
	 * 
	 * @param s1
	 *            the first string
	 * @param s2
	 *            the second string
	 * @return whether or not the two strings are equal
	 */
	private static boolean stringEquals(String s1, String s2) {
		if (s1 == s2) {
			return true;
		}
		if (s1 == null || s2 == null) {
			return false;
		}
		return s1.equals(s2);
	}

	/**
	 * Checks to see if the two objects are both JDP proxies.
	 * 
	 * @param o1
	 *            the first object
	 * @param o2
	 *            the second object
	 * @return whether or not the objects are proxies
	 */
	private static boolean areProxies(Object o1, Object o2) {
		return Proxy.isProxyClass(o1.getClass())
				&& Proxy.isProxyClass(o2.getClass());
	}
}
