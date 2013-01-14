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
package org.apache.sling.commons.proxy.core.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import static org.junit.Assert.*;
import java.util.Date;
import javax.jcr.RepositoryException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.core.SlingEnvironmentHelper;
import org.apache.sling.commons.proxy.api.IJDPFactory;
import org.apache.sling.commons.proxy.api.annotations.OSGiService;
import org.apache.sling.commons.proxy.api.annotations.SlingProperty;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MJKelleher - Dec 24, 2012 9:59:07 AM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.jdp.DefaultJDPImplTest
 */
public class DefaultJDPImplTest {

    @Rule
    public static TestWatcher watch;
    
    private static Logger log;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        SlingEnvironmentHelper.setUp();
        SlingEnvironmentHelper.configureLogging("DEBUG", DefaultJDPImplTest.class.getPackage().getName());
        log = LoggerFactory.getLogger(DefaultJDPImplTest.class);
        watch = SlingEnvironmentHelper.getTestWatcher(log);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() {
        SlingEnvironmentHelper.tearDown();
    }

    @Test
    public void testJDPCreation() throws RepositoryException, URISyntaxException {
        String path = "/content/geometrixx/en/jcr:content";
        JcrContentNode node = newInstance(path, JcrContentNode.class);

        Date d = node.getCq_lastReplicated();
        assertTrue("Last Replicated returned NULL", d != null);

        String title = node.getJcr_title();
        assertTrue("JCR Title returned NULL", title != null);
    }

    @Test
    public void testJDPAnnotationsCreation() throws RepositoryException, URISyntaxException {
        log.debug("");
        String path = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node = newInstance(path, JcrContentNode2.class);

        Date d = node.getLastReplicated();
        assertTrue("Last Replicated returned NULL", d != null);

        String title = node.getTitle();
        assertTrue("JCR Title returned NULL", title != null);

        String htitle = node.getHeaderTitle();
        assertTrue("Header Title returned NULL", htitle != null);

        String imgPath = node.getImagePath();
        assertTrue("Image Path returned NULL", imgPath != null);
    }

    @Test
    public void testJDPAnnotationsCreationToString() throws RepositoryException, URISyntaxException {
        String path = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node = newInstance(path, JcrContentNode2.class);

        String str = node.toString();
        assertTrue("toString return a null value", str != null);
        log.debug(str);
    }

    @Test
    public void testHashCodeDifferentObjects() throws RepositoryException, URISyntaxException {
        String path1 = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);

        String path2 = "/content/geometrixx/jcr:content";
        JcrContentNode2 node2 = newInstance(path2, JcrContentNode2.class);

        log.debug("Hashcode 1 {} and Hashcode 2 {}", node1.hashCode(), node2.hashCode());
        assertTrue("The 2 hashcodes for 2 different Objects were equal", node1.hashCode() != node2.hashCode());
    }

    @Test
    public void testHashCodeSameObjects() throws RepositoryException, URISyntaxException {
        String path1 = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);

        String path3 = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node3 = newInstance(path3, JcrContentNode2.class);

        log.debug("Hashcode 1 {} and Hashcode 3 {}", node1.hashCode(), node3.hashCode());
        assertTrue("The 2 hashcodes for the same Object were not equal", node1.hashCode() == node3.hashCode());
    }

    @Test
    public void testHashCodeSameResourceDifferentInterfaces() throws RepositoryException, URISyntaxException {
        String path1 = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);

        String path3 = "/content/geometrixx/en/jcr:content";
        JcrContentNode node3 = newInstance(path3, JcrContentNode.class);

        int hc1 = node1.hashCode();
        int hc3 = node3.hashCode();

        log.debug("Hashcode 1 {} and Hashcode 3 {}", hc1, hc3);
        assertTrue("The 2 hashcodes for the same Object for different JDP Interfaces were equal", node1.hashCode() != node3.hashCode());
    }

    @Test
    public void testEqualsDifferentObjects() throws RepositoryException, URISyntaxException {
        String path1 = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);

        String path2 = "/content/geometrixx/jcr:content";
        JcrContentNode2 node2 = newInstance(path2, JcrContentNode2.class);

        assertTrue("The 2 different Objects were equal and should not be", !node1.equals(node2));
    }

    @Test
    public void testEqualsSameObjects() throws RepositoryException, URISyntaxException {
        String path1 = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);

        String path3 = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node3 = newInstance(path3, JcrContentNode2.class);

        assertTrue("The 2 Objects were not equal and should be", node1.equals(node3));
    }

    @Test
    public void testEqualsSameResourceDifferentInterfaces() throws RepositoryException, URISyntaxException {
        String path1 = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);


        String path3 = "/content/geometrixx/en/jcr:content";
        JcrContentNode node3 = newInstance(path3, JcrContentNode.class);

        assertTrue("The 2 Objects for the same resource but different JDP Interfaces were equal and should not be", !node1.equals(node3));
    }

    @Test
    public void testBinaryResource() throws RepositoryException, URISyntaxException, IOException {
        String path1 = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);

        InputStream ips = node1.getGeoCubePDF();
        assertTrue("The GeoCube InputStream was NULL.", ips != null);

        int byteCount = 0;
        for (int read = 0; (read = ips.read()) != -1;) {
            byteCount++;
        }
        ips.close();

        log.debug("Read {} bytes from GeoCubePDF", byteCount);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNoPropertyAnnotation() throws RepositoryException, URISyntaxException {
        String path1 = "/content/geometrixx/en/jcr:content";
        MyFunkyService node1 = newInstance(path1, MyFunkyService.class);
        assertTrue("Proxy was instantiated without SlingProperty annotation.", node1 == null);
    }
    
    @Test
    public void testServiceResolutionAndMethodInvocation() throws RepositoryException, URISyntaxException {
        String path1 = "/content/geometrixx/en/jcr:content";
        JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);
        
        assertTrue("Return type for whatIsTheResourcePath did not match", "DNE".equals(node1.whatIsTheResourcePath()));
        assertTrue("Return type for calculateDepth did not match", -13 == node1.calculateDepth() );
    }
    /**
     * *************************************************************************
     *
     * Convenience Methods
     *
     */
    private static <T> T newInstance(String resource, Class<T> t) throws RepositoryException, URISyntaxException {
        Resource r1 = SlingEnvironmentHelper.getResource(null, null, resource);
        assertTrue("Resource " + resource + " was NULL", r1 != null);

        IJDPFactory factory = new DefaultJDPFactoryImpl();
        T rtn = factory.newInstance(r1, t);
        assertTrue("The returned Proxy instance was NULL", rtn != null);

        return rtn;
    }

    /**
     * *************************************************************************
     *
     * Sample Sling Proxy Interfaces
     *
     */
    private static interface JcrContentNode {

        @SlingProperty
        Date getCq_lastReplicated();

        @SlingProperty
        String getJcr_title();
    }

    @OSGiService(service = MyFunkyService.class, implementation = MyFunkyServiceImpl.class)
    private static interface JcrContentNode2 extends MyFunkyService {

        @SlingProperty(name = "cq:lastReplicated")
        Date getLastReplicated();

        @SlingProperty(name = "jcr:title")
        String getTitle();

        @SlingProperty(path = "header", name = "jcr:title")
        String getHeaderTitle();

        @SlingProperty(path = "par/image", name = "fileReference")
        String getImagePath();

        @SlingProperty(path = "/content/dam/geometrixx/documents/GeoCube_Datasheet.pdf/jcr:content/renditions/original/jcr:content", name = "jcr:data")
        InputStream getGeoCubePDF();
    }

    private static interface MyFunkyService {

        String whatIsTheResourcePath();

        int calculateDepth();
    }

    public static final class MyFunkyServiceImpl implements MyFunkyService {
        
        private JcrContentNode2 instance;

        public String whatIsTheResourcePath() {
            return "DNE";
        }

        public int calculateDepth() {
            return -13;
        }
    }
}
