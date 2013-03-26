package org.apache.sling.commons.proxy.lang;

import static org.junit.Assert.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.BaseSlingProxyTest;
import org.apache.sling.commons.proxy.samples.SlingPropertyProxy;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJDPEquals extends BaseSlingProxyTest {
	private static final Logger log = LoggerFactory.getLogger(TestJDPEquals.class);

	@Test
	public void runTests() {

		log.info("runTests");


		Resource pageResource = resolver.getResource("/content/test");
		SlingPropertyProxy pageProxy = pageResource
				.adaptTo(SlingPropertyProxy.class);


		log.info("Testing Equals functionality");
		String stringed = pageProxy.toString();
		assertNotNull(stringed);
		log.info(stringed);

		log.info("Tests Successful");
	}
}
