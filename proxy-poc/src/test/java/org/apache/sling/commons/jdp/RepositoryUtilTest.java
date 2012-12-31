/**
 * 
 */
package org.apache.sling.commons.jdp;

import static org.junit.Assert.assertTrue;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.jcr.resource.internal.JcrResourceResolverFactoryImpl;
import org.apache.sling.jcr.resource.internal.helper.jcr.JcrResourceProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author MJKelleher  - Dec 17, 2012 11:11:08 PM 
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.RepositoryUtil
 */
public final class RepositoryUtilTest {

	
	@Before
	public void setUp() throws Exception {
		SlingEnvironmentHelper.setUp();
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
	
	@Test public void testGetResourceResolver() throws Exception {
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
	
	@Test public void testGetResourceMethod() throws Exception {
		Resource r = SlingEnvironmentHelper.getResource(null, null, "/content/geometrixx/en/jcr:content");
		assertTrue("Resource is null", r != null);
		Node n = r.adaptTo(Node.class);
		n.getSession().logout();
	}
	
}
