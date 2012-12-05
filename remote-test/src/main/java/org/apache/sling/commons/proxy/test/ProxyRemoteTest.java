package org.apache.sling.commons.proxy.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.junit.annotations.SlingAnnotationsTestRunner;
import org.apache.sling.junit.annotations.TestReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(SlingAnnotationsTestRunner.class)
public class ProxyRemoteTest {

	private static final Logger log = LoggerFactory
			.getLogger(ProxyRemoteTest.class);

	@TestReference
	private ResourceResolverFactory resourceResolverFactory;
	private ResourceResolver resolver;

	@Before
	public void init() {
		try {
			resolver = resourceResolverFactory
					.getAdministrativeResourceResolver(null);
		} catch (org.apache.sling.api.resource.LoginException e) {
			log.error("Unable to get administrative resource resolver", e);
			fail("Unable to get administrative resource resolver: " + e);
		}
	}

	@Test
	public void runTest() {
		try {
			log.info("runTest");
			Iterator<Resource> files = resolver.findResources(
					"/jcr:root/libs//element(*,nt:file) order by @jcr:score",
					"xpath");
			for (int i = 0; i < 10 && files.hasNext(); i++) {
				Resource file = files.next();
				FileProxy fileProxy = file.adaptTo(FileProxy.class);

				log.debug("Processing file: " + file);

				log.debug("Checking to see if file proxy retrieved");
				assertNotNull(fileProxy);

				log.debug("Checking to see if backing resource set");
				assertNotNull(fileProxy.getBackingResource());

				log.debug("Checking to see if contents set");
				assertNotNull(fileProxy.getContents());

				log.debug("Checking to see if primary type set");
				assertNotNull(fileProxy.getPrimaryType());
			}
			log.info("Test successfull");
		} catch (Throwable t) {
			log.warn("Unexpected exception: ", t);
			throw new RuntimeException(t);
		}
	}

}
