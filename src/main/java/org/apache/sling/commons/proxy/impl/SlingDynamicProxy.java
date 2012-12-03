package org.apache.sling.commons.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.sling.api.resource.Resource;

public class SlingDynamicProxy implements InvocationHandler {

	private final Resource resource;

	public SlingDynamicProxy(final Resource resource) {
		this.resource = resource;
	}

	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		// TODO: Handle the invocation target here
		return null;
	}
}
