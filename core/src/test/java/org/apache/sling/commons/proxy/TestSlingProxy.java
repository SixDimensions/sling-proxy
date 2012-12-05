package org.apache.sling.commons.proxy;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.apache.sling.commons.proxy.api.AbstractProxyAdapterFactory;
import org.apache.sling.commons.proxy.api.ProxyAnnotationHandlerManager;
import org.apache.sling.commons.proxy.api.SlingProxy;
import org.apache.sling.commons.proxy.api.SlingProxyService;
import org.apache.sling.commons.proxy.api.annotations.SlingProperty;
import org.apache.sling.commons.proxy.core.impl.ProxyAnnotationHandlerManagerImpl;
import org.apache.sling.commons.proxy.core.impl.SlingPropertyAnnotationHandler;
import org.apache.sling.commons.proxy.core.impl.SlingProxyServiceImpl;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSlingProxy {

	private static final String TITLE = "My Title";
	private static final String CONTENT_RESOURCE_TYPE = "myapp/components/page";
	private static final String PAGE_RESOURCE_TYPE = "myapp:Page";
	private final static Logger log = LoggerFactory
			.getLogger(TestSlingProxy.class);
	private MockResourceResolver resolver;

	@Before
	public void init() throws Exception {
		log.info("init");

		log.info("Initializing services");
		ProxyAnnotationHandlerManager proxyAnnotationServiceManager = new ProxyAnnotationHandlerManagerImpl();
		proxyAnnotationServiceManager.registerProxyAnnotationHandler(
				SlingProperty.class, new SlingPropertyAnnotationHandler());

		final AbstractProxyAdapterFactory proxyAdaptorFactory = new AbstractProxyAdapterFactory() {
		};

		SlingProxyService slingProxyService = new SlingProxyServiceImpl();

		log.info("Injecting proxyAnnotationHandlerManager");
		Field proxyAnnotationServiceManagerField = SlingProxyServiceImpl.class
				.getDeclaredField("proxyAnnotationServiceManager");
		proxyAnnotationServiceManagerField.setAccessible(true);
		proxyAnnotationServiceManagerField.set(slingProxyService,
				proxyAnnotationServiceManager);

		log.info("Injecting classLoaderManager");
		Field classLoaderManagerField = SlingProxyServiceImpl.class
				.getDeclaredField("classLoaderManager");
		classLoaderManagerField.setAccessible(true);
		classLoaderManagerField.set(slingProxyService,
				new DynamicClassLoaderManager() {
					public ClassLoader getDynamicClassLoader() {
						return this.getClass().getClassLoader();
					}
				});

		log.info("Injecting Sling Proxy Service");
		Field slingProxyServiceField = AbstractProxyAdapterFactory.class
				.getDeclaredField("slingProxyService");
		slingProxyServiceField.setAccessible(true);
		slingProxyServiceField.set(proxyAdaptorFactory, slingProxyService);

		log.info("Creating Mock Resources");
		resolver = new MockResourceResolver();
		final MockResource pageResource = new MockResource(resolver,
				"/content/test", PAGE_RESOURCE_TYPE) {
			public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
				if (SlingProxy.class.isAssignableFrom(type)) {
					return (AdapterType) proxyAdaptorFactory.getAdapter(this,
							type);
				} else {
					return super.adaptTo(type);
				}
			}
		};
		pageResource.addProperty("sling:resourceType", PAGE_RESOURCE_TYPE);
		resolver.addResource(pageResource);
		final MockResource contentResource = new MockResource(resolver,
				"/content/test/jcr:content", CONTENT_RESOURCE_TYPE) {
			public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
				if (SlingProxy.class.isAssignableFrom(type)) {
					return (AdapterType) proxyAdaptorFactory.getAdapter(this,
							type);
				} else {
					return super.adaptTo(type);
				}
			}
		};
		contentResource.addProperty("jcr:title", TITLE);
		contentResource
				.addProperty("sling:resourceType", CONTENT_RESOURCE_TYPE);
		contentResource.addProperty("active", false);
		resolver.addResource(contentResource);

		log.info("Initialization complete");
	}

	@Test
	public void runTests() {

		log.info("runTests");

		Resource resource = resolver.getResource("/content/test/jcr:content");
		SimpleSlingProxy simpleSlingProxy = resource
				.adaptTo(SimpleSlingProxy.class);

		log.info("Testing backing resource");
		assertEquals(simpleSlingProxy.getBackingResource(), resource);

		log.info("Testing property retrieval");
		assertEquals(TITLE, simpleSlingProxy.getJcrTitle());
		assertEquals(CONTENT_RESOURCE_TYPE,
				simpleSlingProxy.getSlingResourceType());
		assertEquals(false, simpleSlingProxy.isActive());
		assertEquals("false", simpleSlingProxy.active());
		assertEquals(TITLE, simpleSlingProxy.jcrTitle());
		assertEquals(null, simpleSlingProxy.getNonExistentProperty());

		log.info("Testing Sling Property");
		Resource pageResource = resolver.getResource("/content/test");
		SlingPropertyProxy pageProxy = pageResource
				.adaptTo(SlingPropertyProxy.class);
		assertEquals(TITLE, pageProxy.getTitle());
		assertEquals(PAGE_RESOURCE_TYPE, pageProxy.getSlingResourceType());
		assertEquals(null, pageProxy.getNonExistentProperty());
		assertEquals(false, pageProxy.isActive());

		log.info("Tests Successful");
	}
}
