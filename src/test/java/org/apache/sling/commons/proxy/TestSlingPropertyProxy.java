/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.commons.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.samples.NoAnnotationSlingProxy;
import org.apache.sling.commons.proxy.samples.SlingPropertyProxy;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests the Sling Proxy retrieval of properties. 
 */
public class TestSlingPropertyProxy extends BaseSlingProxyTest {
	private static final Logger log = LoggerFactory
			.getLogger(TestSlingPropertyProxy.class);

	@Test
	public void runTests() {

		log.info("runTests");

		Resource resource = resolver.getResource("/content/test/jcr:content");
		try {
			NoAnnotationSlingProxy simpleSlingProxy = resource
					.adaptTo(NoAnnotationSlingProxy.class);
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
