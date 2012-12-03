package org.apache.sling.commons.proxy.impl;

import java.lang.reflect.Proxy;

import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;

public class ProxyAdapterFactory implements AdapterFactory {

	@Reference
	private DynamicClassLoaderManager classLoaderManager;

	public <AdapterType> AdapterType getAdapter(Object adaptable,
			Class<AdapterType> type) {

		if (adaptable instanceof Resource) {

			ClassLoader classLoader = classLoaderManager
					.getDynamicClassLoader();

			Resource resource = (Resource) adaptable;
			return (AdapterType) Proxy.newProxyInstance(classLoader,
					new Class[] { type.getClass() }, new SlingDynamicProxy(
							resource));
		}
		return null;

	}
}
