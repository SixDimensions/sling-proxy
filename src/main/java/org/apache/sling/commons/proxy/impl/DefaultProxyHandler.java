package org.apache.sling.commons.proxy.impl;

import java.lang.reflect.Method;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.ProxyAnnotationService;
import org.apache.sling.commons.proxy.SlingProxy;

public class DefaultProxyHandler implements ProxyAnnotationService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.commons.proxy.ProxyAnnotationService#invoke(org.apache
	 * .sling.api.resource.Resource, org.apache.sling.commons.proxy.SlingProxy,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Resource resource, SlingProxy proxy, Method m,
			Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}
}
