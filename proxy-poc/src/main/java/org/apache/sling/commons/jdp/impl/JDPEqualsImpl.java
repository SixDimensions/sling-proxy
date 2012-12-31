/**
 * 
 */
package org.apache.sling.commons.jdp.impl;

import java.lang.reflect.Proxy;

import org.apache.sling.commons.reflection.Classes;
import org.apache.sling.commons.lang.IEquals;

/**
 * @author MJKelleher  - Dec 27, 2012 12:58:32 AM 
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.jdp.impl.JDPEqualsImpl
 */
final class JDPEqualsImpl implements IEquals {

	public boolean equals(Object o1, Object o2) {
		if (o1 == o2) return true;
		if (o1 == null || o2 == null) return false;
		if (! areProxies(o1, o2)) return false;
		
		try {
			ResourceInvocationHandler rih1 = (ResourceInvocationHandler) Proxy.getInvocationHandler(o1);
			ResourceInvocationHandler rih2 = (ResourceInvocationHandler) Proxy.getInvocationHandler(o2);
			if (Classes.haveSameInterfaces(o1, o2)) {
				return _equals(rih1.getResourcePath(), rih2.getResourcePath());
			}
		} catch (ClassCastException ex) {
		}
		return false;
	}
	
	private static boolean _equals(String s1, String s2) {
		if (s1 == s2) return true;
		if (s1 == null || s2 == null) return false;
		return s1.equals(s2);
	}

	private static boolean areProxies(Object o1, Object o2) {
		return Proxy.isProxyClass(o1.getClass()) && Proxy.isProxyClass(o2.getClass());
	}
}
