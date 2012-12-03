package org.apache.sling.commons.proxy;

public interface ProxyAnnotationServiceManager {
	public void registerProxyAnnotationService(Class<?> annotationClass,
			ProxyAnnotationService service);

	public ProxyAnnotationService getProxyAnnotationService(Class<?> annotation);
}