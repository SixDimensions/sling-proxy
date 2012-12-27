/**
 * 
 */
package org.apache.sling.commons.jdp.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.jdp.api.IJDPFactory;

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

}
