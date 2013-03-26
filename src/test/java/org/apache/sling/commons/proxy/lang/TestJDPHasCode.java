package org.apache.sling.commons.proxy.lang;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.BaseSlingProxyTest;
import org.apache.sling.commons.proxy.samples.DuplicateSlingPropertyProxy;
import org.apache.sling.commons.proxy.samples.SlingPropertyProxy;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJDPHasCode extends BaseSlingProxyTest {
	private static final Logger log = LoggerFactory
			.getLogger(TestJDPHasCode.class);

	@Test
	public void runTests() {

		log.info("runTests");

		Resource pageResource = resolver.getResource("/content/test");
		SlingPropertyProxy pageProxy = pageResource
				.adaptTo(SlingPropertyProxy.class);

		log.info("Testing Equals functionality");
		int hashCode = pageProxy.hashCode();
		assertNotNull(hashCode);
		log.info("HashCode: " + hashCode);

		log.info("Ensuring the same proxy interface from the same resource get the same hash code");
		SlingPropertyProxy pageProxy2 = pageResource
				.adaptTo(SlingPropertyProxy.class);
		log.info("HashCode1 {}, HashCode 2 {}", hashCode, pageProxy2.hashCode());
		assertEquals(pageProxy2.hashCode(), hashCode);

		log.info("Ensuring different proxy interfaces from the same resource get different hash codes");
		DuplicateSlingPropertyProxy pageProxy3 = pageResource
				.adaptTo(DuplicateSlingPropertyProxy.class);
		log.info("HashCode1 {}, HashCode 2 {}", hashCode, pageProxy3.hashCode());
		assertFalse(pageProxy3.hashCode() == hashCode);

		log.info("Tests Successful");
	}
}
