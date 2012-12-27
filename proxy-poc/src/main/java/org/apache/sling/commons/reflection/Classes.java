/**
 * 
 */
package org.apache.sling.commons.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Set;

/**
 * @author kellehmj - Dec 25, 2012 2:59:42 PM 
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.api.reflection.Classes
 */
public final class Classes {
	private Classes() {}

	@SuppressWarnings("rawtypes")
	public static List<Field> getFields(Object obj) {
		java.util.ArrayList<Field> list = new java.util.ArrayList<Field>();
		
		Class c = obj.getClass();
		for ( ; c != null && c != Object.class ; ) {
			for (Field f : c.getDeclaredFields()) {
				if (! f.isSynthetic()) {
					list.add(f);
				}
			}
			c = c.getSuperclass();
		}
		
		list.trimToSize();
		return list;
	}
	
	/**
	 * To have the same set of interfaces the two Objects must be one of the following:
	 * 		> be equal in identity to one another, or both NULL
	 *      > have 0 interfaces and either be Proxy Classes or be the same Type
	 *      > must have the same number of interfaces, and the same interfaces 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean haveSameInterfaces(Object o1, Object o2) {
		if (o1 == o2) return true;
		
		Class[] ca1 = o1.getClass().getInterfaces();
		Class[] ca2 = o2.getClass().getInterfaces();
		int size1 = size(ca1); 
		if (size1 != size(ca2)) return false;
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
