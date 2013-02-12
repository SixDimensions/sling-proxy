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

import java.lang.reflect.Method;

/**
 * Generates a String representation of an object.
 */
public final class JDPToStringImpl {

	/**
	 * Generates a String representation of an object.
	 * 
	 * @param obj
	 *            the object to generate a string of
	 * @return the string representation
	 */
	public String toString(Object obj) {
		StringBuilder sb = new StringBuilder(256);

		if (obj != null) {
			if (!supportsToString(obj, sb)) {
				handleByType(obj, sb);
			}
		} else {
			sb.append("null");
		}

		return sb.toString();
	}

	/**
	 * Checks to see if the specified object contains a dedicated toString
	 * method.
	 * 
	 * @param obj
	 *            the object to check
	 * @param sb
	 *            container for the results
	 * @return whether or not the object has a dedicated toString method
	 */
	private boolean supportsToString(Object obj, StringBuilder sb) {
		try {
			Method m = obj.getClass().getDeclaredMethod("toString",
					(Class[]) null);
			try {
				Object result = m.invoke(obj, (Object[]) null);
				sb.append(result);
			} catch (Exception ex) {
				sb.append("caused - ").append(ex.getClass().getName())
						.append(" - Message = ").append(ex.getMessage());
			}
			return true;
		} catch (SecurityException ex) {
		} catch (NoSuchMethodException ex) {
		}

		return false;
	}

	/**
	 * Generates a toString based on the objects classes and methods.
	 * 
	 * @param obj
	 *            the object to generate the string for
	 * @param sb
	 *            the string builder to write to
	 */
	private void handleByType(Object obj, StringBuilder sb) {
		if (obj.getClass().isArray()) {
			Object[] oa = (Object[]) obj;
			sb.append("[ ");
			for (int ndx = 0, max = oa.length; ndx < max; ndx++) {
				sb.append(toString(oa[ndx]));
				if (ndx + 1 < max) {
					sb.append(" , ");
				}
			}
			sb.append(" ]");
		} else {
			sb.append(obj);
		}
	}
}
