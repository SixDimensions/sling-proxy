/**
 * 
 */
package org.apache.sling.commons.reflection;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author MJKelleher  - Dec 27, 2012 5:44:47 PM 
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.reflection.Annotations
 */
public final class Annotations {
	private Annotations() { }
	
	public static <T extends Annotation> Set<T> get(Class c, Class<T> annType) {
		java.util.HashSet<T> set = new java.util.HashSet<T>();
		
		Annotation[] anns = c.getAnnotations();
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
