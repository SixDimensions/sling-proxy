/**
 * 
 */
package org.apache.sling.commons.jdp.impl;

import static org.junit.Assert.*;

import java.util.Date;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.jdp.SlingEnvironmentHelper;
import org.apache.sling.commons.jdp.api.IJDPFactory;
import org.apache.sling.commons.jdp.api.annotations.OSGiService;
import org.apache.sling.commons.jdp.api.annotations.SlingProperty;
import org.apache.sling.commons.jdp.impl.DefaultJDPFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kellehmj - Dec 24, 2012 9:59:07 AM 
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.jdp.DefaultJDPImplTest
 */
public class DefaultJDPImplTest {
	private Logger log;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		SlingEnvironmentHelper.setUp();
		SlingEnvironmentHelper.configureLogging("DEBUG", this.getClass().getPackage().getName());
		log = LoggerFactory.getLogger(DefaultJDPImplTest.class);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		SlingEnvironmentHelper.tearDown();
	}

	@Test
	public void testJDPCreation() throws Exception {
		String path = "/content/geometrixx/en/jcr:content";
		JcrContentNode node = newInstance(path, JcrContentNode.class);
		
		Date d = node.getCq_lastReplicated();
		assertTrue("Last Replicated returned NULL", d != null);
		
		String title = node.getJcr_title();
		assertTrue("JCR Title returned NULL", title != null);
	}
	
	@Test
	public void testJDPAnnotationsCreation() throws Exception {
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
	public void testJDPAnnotationsCreationToString() throws Exception {
		String path = "/content/geometrixx/en/jcr:content";
		JcrContentNode2 node = newInstance(path, JcrContentNode2.class);
		
		String str = node.toString();
		assertTrue("toString return a null value", str != null);
		log.debug(str);
	}
	
	@Test
	public void testHashCodeDifferentObjects() throws Exception {
		String path1 = "/content/geometrixx/en/jcr:content";
		JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);
		
		String path2 = "/content/geometrixx/jcr:content";
		JcrContentNode2 node2 = newInstance(path2, JcrContentNode2.class);
		
		log.debug("Hashcode 1 {} and Hashcode 2 {}", node1.hashCode(), node2.hashCode());
		assertTrue("The 2 hashcodes for 2 different Objects were equal", node1.hashCode() != node2.hashCode());
	}

	@Test
	public void testHashCodeSameObjects() throws Exception {
		String path1 = "/content/geometrixx/en/jcr:content";
		JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);
		
		String path3 = "/content/geometrixx/en/jcr:content";
		JcrContentNode2 node3 = newInstance(path3, JcrContentNode2.class);
		
		log.debug("Hashcode 1 {} and Hashcode 3 {}", node1.hashCode(), node3.hashCode());
		assertTrue("The 2 hashcodes for the same Object were not equal", node1.hashCode() == node3.hashCode());
	}
	
	@Test
	public void testHashCodeSameResourceDifferentInterfaces() throws Exception {
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
	public void testEqualsDifferentObjects() throws Exception {
		String path1 = "/content/geometrixx/en/jcr:content";
		JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);
		
		String path2 = "/content/geometrixx/jcr:content";
		JcrContentNode2 node2 = newInstance(path2, JcrContentNode2.class);
		
		assertTrue("The 2 different Objects were equal and should not be", ! node1.equals(node2) );
	}
	
	@Test
	public void testEqualsSameObjects() throws Exception {
		String path1 = "/content/geometrixx/en/jcr:content";
		JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);
		
		String path3 = "/content/geometrixx/en/jcr:content";
		JcrContentNode2 node3 = newInstance(path3, JcrContentNode2.class);
		
		assertTrue("The 2 Objects were not equal and should be", node1.equals(node3));
	}
	
	@Test
	public void testEqualsSameResourceDifferentInterfaces() throws Exception {
		String path1 = "/content/geometrixx/en/jcr:content";
		JcrContentNode2 node1 = newInstance(path1, JcrContentNode2.class);		
		
		
		String path3 = "/content/geometrixx/en/jcr:content";
		JcrContentNode node3 = newInstance(path3, JcrContentNode.class);
		
		assertTrue("The 2 Objects for the same resource but different JDP Interfaces were equal and should not be", ! node1.equals(node3));
	}
	
	private static <T> T newInstance(String resource, Class<T>... t) throws Exception {
		Resource r1 = SlingEnvironmentHelper.getResource(null, null, resource);
		assertTrue("Resource " + resource + " was NULL", r1 != null);
		
		IJDPFactory factory = new DefaultJDPFactoryImpl();
		T rtn = factory.newInstance(r1, t);
		assertTrue("The returned Proxy instance was NULL", rtn != null);
		
		return rtn;
	}
	
	private static interface JcrContentNode {
		Date getCq_lastReplicated();
		String getJcr_title();
	}
	
	@OSGiService(service=MyFunkyService.class, implementation=MyFunkyServiceImpl.class)
	private static interface JcrContentNode2 extends JcrContentNode3, JcrContentNode4, MyFunkyService {
	}
	
	private static interface MyFunkyService {
		String whatIsTheResourcePath();
		int calculateDepth();
	}
	
	private static interface JcrContentNode3 {
		@SlingProperty(path="cq:lastReplicated")
		Date getLastReplicated();
		@SlingProperty(path="jcr:title")
		String getTitle();
	}
	
	private static interface JcrContentNode4 {
		@SlingProperty(path="header/jcr:title")
		String getHeaderTitle();
		@SlingProperty(path="par/image/fileReference")
		String getImagePath();
	}
	
	private static final class MyFunkyServiceImpl implements MyFunkyService {
		private JcrContentNode3 instance;
		private JcrContentNode4 instance2;
		
		public String whatIsTheResourcePath() {
			return null;
		}
		public int calculateDepth() {
			return -1;
		}
	}
}
