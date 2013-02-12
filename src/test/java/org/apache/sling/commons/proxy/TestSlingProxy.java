package org.apache.sling.commons.proxy;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.apache.sling.commons.proxy.annotations.SlingProperty;
import org.apache.sling.commons.proxy.impl.DefaultSlingProxyServiceImpl;
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
	private DefaultSlingProxyServiceImpl slingProxyService;

	@Before
	public void init() throws Exception {
		log.info("init");

		log.info("Initializing services");
		final AbstractProxyAdapterFactory proxyAdaptorFactory = new AbstractProxyAdapterFactory();

		slingProxyService = new DefaultSlingProxyServiceImpl();

		log.info("Creating Mock Resources");
		resolver = new MockResourceResolver();
		final MockResource pageResource = new MockResource(resolver,
				"/content/test", PAGE_RESOURCE_TYPE) {
			public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
				if (ISlingProxy.class.isAssignableFrom(type)) {
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
				if (ISlingProxy.class.isAssignableFrom(type)) {
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
