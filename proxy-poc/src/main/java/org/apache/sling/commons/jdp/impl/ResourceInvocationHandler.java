/**
 * 
 */
package org.apache.sling.commons.jdp.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.reflection.DefaultHashCodeImpl;
import org.apache.sling.commons.reflection.GetMethodToStringImpl;
import org.apache.sling.commons.reflection.IEquals;
import org.apache.sling.commons.reflection.IHashCode;
import org.apache.sling.commons.reflection.PrimeNumber;

/**
 * @author kellehmj - Dec 23, 2012 11:27:36 PM 
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.jdp.ResourceInvocationHandler
 */
final class ResourceInvocationHandler implements InvocationHandler {
	
	@SuppressWarnings("unused")
	private final Resource r;
	private final Node node;
	ResourceInvocationHandler(Resource r) {
		this.r = r;
		this.node = r.adaptTo(Node.class);
	}
	/**************************************************************************
	 * 
	 * 
	 */
	
	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		InvokedTO to = InvokedTO.newInstance(proxy, method, args);
		if (to.isGetter()) {
			return (handleGet(to));
		} else if (to.isType(MethodType.JavaBeanSet)) {
			return null;
		} else if (to.isType(MethodType.ToString)) {
			return new GetMethodToStringImpl().toString(proxy);
		} else if (to.isType(MethodType.HashCode)) {
			IHashCode hc = new DefaultHashCodeImpl();
			return hc.hashCode(proxy);
		} else if (to.isType(MethodType.Equals)) {
			if (args == null || args.length != 1) {
				String msg = "Method 'equals' requires exactly 1 argument.";
				throw new IllegalArgumentException(msg);
			}
			IEquals ieq = new JDPEqualsImpl();
			return ieq.equals(proxy, args[0]);
		}
		throw new NoSuchMethodException("Method " + method.getName() + " DNE");
	}
	
	@Override
	public int hashCode() {
		int hashCode = ResourceInvocationHandler.class.hashCode() * PrimeNumber.getInstance().get(2) + r.getPath().hashCode();
		return hashCode;
	}
	/**************************************************************************
	 * 
	 * 
	 */
	
	final String getResourcePath() {
		return r.getPath();
	}
	
	/**************************************************************************
	 * 
	 * 
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object handleGet(InvokedTO to) throws Throwable {
		Property p;
		try {
			p = node.getProperty(to.beanName);
		} catch (PathNotFoundException ex) {
			return null;
		}
		
		Class type = to.method.getReturnType();
		if (p.isMultiple()) {
			return PropertyHandler.castPropertyArray(p, type);
		}
		return PropertyHandler.castProperty(p, type);
	}

}
