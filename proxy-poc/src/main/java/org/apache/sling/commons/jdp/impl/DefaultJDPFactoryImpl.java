/**
 * 
 */
package org.apache.sling.commons.jdp.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.jdp.api.IJDPFactory;
import org.apache.sling.commons.jdp.api.annotations.OSGiService;
import org.apache.sling.commons.reflection.Annotations;

/**
 * @author kellehmj - Dec 24, 2012 9:53:39 AM 
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.jdp.DefaultJDPImpl
 */
public final class DefaultJDPFactoryImpl implements IJDPFactory {
	
	public <T> T newInstance(Resource r, Class<T> type) {
		if (r == null) {
			throw new NullPointerException("The Resource cannot be NULL.");
		}
		if (type == null) {
			throw new NullPointerException("The Provided Interface cannot be NULL.");
		}
		if (! type.isInterface()) {
			String msg = "Class " + type.getName() + " should have been an Interface.";
			throw new IllegalStateException(msg);
		}
		
		InvocationHandler ih = new ResourceInvocationHandler(r);
		T rtn = (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, ih);
		return rtn;
	}
	
	private static final Set<Class> getServiceInterfaces(Class type) {
		Set<Class> svcs = new java.util.HashSet<Class>();
		
		Class[] interfaces = type.getInterfaces();
		
		if (interfaces != null && interfaces.length > 0) {
			Set<Class> set = getServiceInterfaces(Annotations.get(type, OSGiService.class));
			
			for (Class interfce : interfaces) {
				if (set.contains(interfce)) {
					svcs.add(interfce);
				}
			}
		}
		return svcs;
	}
	
	private static final Set<Class> getServiceInterfaces(Set<OSGiService> set) {
		Set<Class> rtn = new java.util.HashSet<Class>();
		if (set != null || set.size() > 0) {
			for (OSGiService svc : set) {
				rtn.add(svc.service());
			}
		}
		return rtn;
	}
}
