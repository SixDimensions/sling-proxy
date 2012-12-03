package org.apache.sling.commons.proxy.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.commons.proxy.ProxyAnnotationService;
import org.apache.sling.commons.proxy.ProxyAnnotationServiceManager;

public class ProxyAnnotationServiceManagerImpl implements
		ProxyAnnotationServiceManager {
	private Map<Class<?>, ProxyAnnotationService> annocationServiceCache = new HashMap<Class<?>, ProxyAnnotationService>();

	public ProxyAnnotationService getProxyAnnotationService(Class<?> annotation) {
		return annocationServiceCache.get(annotation);
	}

	public void registerProxyAnnotationService(Class<?> annotationClass,
			ProxyAnnotationService service) {
		annocationServiceCache.put(annotationClass, service);
	}
}
