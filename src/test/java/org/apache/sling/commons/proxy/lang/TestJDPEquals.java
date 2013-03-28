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
package org.apache.sling.commons.proxy.lang;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.BaseSlingProxyTest;
import org.apache.sling.commons.proxy.ISlingProxyService;
import org.apache.sling.commons.proxy.impl.DefaultSlingProxyServiceImpl;
import org.apache.sling.commons.proxy.samples.DuplicateSlingPropertyProxy;
import org.apache.sling.commons.proxy.samples.SlingPropertyProxy;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for the Sling Proxy hashCode implementation.
 */
public class TestJDPEquals extends BaseSlingProxyTest {
	private static final Logger log = LoggerFactory
			.getLogger(TestJDPEquals.class);

	/**
	 * All of the tests for the hashCode implementation.
	 */
	@Test
	public void runTests() {

		log.info("runTests");

		Resource pageResource = resolver.getResource("/content/test");
		SlingPropertyProxy pageProxy = pageResource
				.adaptTo(SlingPropertyProxy.class);

		log.info("Testing Equals functionality");

		log.info("Ensuring the same proxy interface from the same resource are equal");
		SlingPropertyProxy pageProxy2 = pageResource
				.adaptTo(SlingPropertyProxy.class);
		assertTrue(pageProxy.equals(pageProxy2));

		log.info("Ensuring different proxy interfaces from the same resource are not equal");
		DuplicateSlingPropertyProxy pageProxy3 = pageResource
				.adaptTo(DuplicateSlingPropertyProxy.class);
		assertFalse(pageProxy.equals(pageProxy3));


		log.info("Ensuring retrieving the same proxy from different proxy services are equal");
		ISlingProxyService slingProxyService2 = new DefaultSlingProxyServiceImpl();
		SlingPropertyProxy pageProxy4 = slingProxyService2.getProxy(
				pageResource, SlingPropertyProxy.class);
		assertTrue(pageProxy.equals(pageProxy4));
		
		log.info("Ensuring the same proxy interfaces from the differet resources are not equal");
		Resource pageContentResource = resolver.getResource("/content/test/jcr:content");
		SlingPropertyProxy pageProxy5 = pageContentResource
				.adaptTo(SlingPropertyProxy.class);
		assertFalse(pageProxy.equals(pageProxy5));

		log.info("Tests Successful");
	}
}
