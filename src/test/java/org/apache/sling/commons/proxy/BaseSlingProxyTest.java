package org.apache.sling.commons.proxy;

import org.apache.sling.commons.proxy.impl.DefaultSlingProxyServiceImpl;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseSlingProxyTest {
	public static final String TITLE = "My Title";
	public static final String CONTENT_RESOURCE_TYPE = "myapp/components/page";
	public static final String PAGE_RESOURCE_TYPE = "myapp:Page";
	private final static Logger log = LoggerFactory
			.getLogger(TestSlingProxy.class);
	protected final MockResourceResolver resolver = new MockResourceResolver();
	protected final ISlingProxyService slingProxyService = new DefaultSlingProxyServiceImpl();

	@Before
	public void init() throws Exception {
		log.info("init");

		log.info("Creating Mock Resources");
		final MockResource pageResource = new MockResource(resolver,
				"/content/test", PAGE_RESOURCE_TYPE) {
			public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
				if (ISlingProxy.class.isAssignableFrom(type)) {
					return slingProxyService.getProxy(this, type);
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
					return slingProxyService.getProxy(this, type);
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
}
