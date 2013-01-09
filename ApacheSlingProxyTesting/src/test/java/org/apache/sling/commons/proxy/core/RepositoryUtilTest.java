/*
 * Copyright 2012 Six Dimensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.commons.proxy.core;

import java.io.IOException;
import java.net.URISyntaxException;
import static org.junit.Assert.assertTrue;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.sling.api.resource.*;
import org.apache.sling.jcr.resource.internal.JcrResourceResolverFactoryImpl;
import org.apache.sling.jcr.resource.internal.helper.jcr.JcrResourceProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MJKelleher - Dec 17, 2012 11:11:08 PM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.RepositoryUtil
 */
public final class RepositoryUtilTest {
    private Logger log;

    @Before
    public void setUp() throws Exception {
        SlingEnvironmentHelper.setUp();
        SlingEnvironmentHelper.configureLogging("DEBUG", this.getClass().getName());
        log = LoggerFactory.getLogger(RepositoryUtilTest.class);
    }

    @After
    public void tearDown() throws Exception {
        SlingEnvironmentHelper.tearDown();
    }

    @Test
    public void testLoginSession() throws Exception {
        Session s = null;
        try {
            s = SlingEnvironmentHelper.newSession(null, null);
            assertTrue("JCR Session was NULL.", s != null);
            Node n = s.getNode("/content");
            assertTrue("Node /content was NULL.", n != null);
        } finally {
            if (s != null && s.isLive()) {
                s.logout();
            }
        }
    }

    @Test
    public void testGetResourceResolver() throws Exception {
        Session s = null;
        try {
            s = SlingEnvironmentHelper.newSession(null, null);
            assertTrue("JCR Session was NULL.", s != null);
            JcrResourceProvider p = new JcrResourceProvider(s, RepositoryUtilTest.class.getClassLoader(), true);
            assertTrue("JcrResourceProvider is null", p != null);

            JcrResourceResolverFactoryImpl rrf = new JcrResourceResolverFactoryImpl();
            assertTrue("JcrResourceResolverFactoryImpl is null", rrf != null);

            ResourceResolver rr = rrf.getResourceResolver(s);
            assertTrue("ResourceResolver is null", rr != null);

            Resource r = rr.getResource("/content/geometrixx/en/jcr:content");
            assertTrue("Resource is null", r != null);

            ValueMap vm = ResourceUtil.getValueMap(r);
            assertTrue("ValueMap is null", vm != null);

            String pt = vm.get("pageTitle", String.class);
            assertTrue("PageTitle is null", pt != null);
            assertTrue("PageTitle is not equal to GeoMetrixx", pt.equals("GeoMetrixx"));
        } finally {
            if (s != null && s.isLive()) {
                s.logout();
            }
        }
    }

    @Test
    public void testGetResourceMethod() throws Exception {
        Resource r = SlingEnvironmentHelper.getResource(null, null, "/content/geometrixx/en/jcr:content");
        assertTrue("Resource is null", r != null);
        Node n = r.adaptTo(Node.class);
        n.getSession().logout();
    }
    
    @Test
    public void testResourceMetaData() throws RepositoryException, URISyntaxException, IOException {
        String path = "/content/dam/geometrixx/documents/GeoPyramid_Datasheet.pdf/jcr:content/renditions/original/jcr:content";
        Resource r = SlingEnvironmentHelper.getResource(null, null, path);
        assertTrue("Resource is null", r != null);
        
        ResourceMetadata rm = r.getResourceMetadata();
        for (String key : rm.keySet()) {
            log.debug("Key {} Value {}", key, rm.get(key));
        }
        
        ValueMap vm = r.adaptTo(ValueMap.class);
        
        java.io.InputStream ips = vm.get("jcr:data", java.io.InputStream.class);
        assertTrue("InputStream was NULL", ips != null);
        
        int count = 0;
        for (int byt = -1 ; (byt = ips.read()) > -1 ; ) {
            count++;
        }
        ips.close();
        log.debug("Number of bytes read was {}", count);
        assertTrue("Did not read ANY bytes, and wrong length ", count > 1);
    }
}
