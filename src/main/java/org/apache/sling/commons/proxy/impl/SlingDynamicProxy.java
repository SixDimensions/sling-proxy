package org.apache.sling.commons.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.ProxyAnnotationServiceManager;

public class SlingDynamicProxy implements InvocationHandler {

	private final Resource resource;
	private final ProxyAnnotationServiceManager proxyAnnotationServiceManager;

	public SlingDynamicProxy(final Resource resource,
			final ProxyAnnotationServiceManager proxyAnnotationServiceManager) {
		this.resource = resource;
		this.proxyAnnotationServiceManager = proxyAnnotationServiceManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		// TODO: Handle the invocation target here
		return null;
	}
}
