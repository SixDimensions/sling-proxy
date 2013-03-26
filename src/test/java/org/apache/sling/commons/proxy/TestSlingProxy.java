package org.apache.sling.commons.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.samples.SimpleSlingProxy;
import org.apache.sling.commons.proxy.samples.SlingPropertyProxy;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSlingProxy extends BaseSlingProxyTest {
	private static final Logger log = LoggerFactory
			.getLogger(TestSlingProxy.class);

	@Test
	public void runTests() {

		log.info("runTests");

		Resource resource = resolver.getResource("/content/test/jcr:content");
		try {
			SimpleSlingProxy simpleSlingProxy = resource
					.adaptTo(SimpleSlingProxy.class);
			fail("Expected UnsupportedOperationException getting: "
					+ simpleSlingProxy);
		} catch (UnsupportedOperationException uoe) {
			log.debug("Passed initial test");
		}

		Resource pageResource = resolver.getResource("/content/test");
		SlingPropertyProxy pageProxy = pageResource
				.adaptTo(SlingPropertyProxy.class);

		log.info("Testing backing resource");
		assertEquals(pageProxy.getBackingResource(), pageResource);

		log.info("Testing property retrieval");
		assertEquals(TITLE, pageProxy.getTitle());
		assertEquals(PAGE_RESOURCE_TYPE, pageProxy.getSlingResourceType());
		assertEquals(null, pageProxy.getNonExistentProperty());
		assertEquals(false, pageProxy.isActive());

		log.info("Tests Successful");
	}
}
