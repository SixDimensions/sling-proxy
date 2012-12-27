package org.apache.sling.commons.jdp.impl;

import static org.junit.Assert.*;

import java.util.Date;

import javax.jcr.Property;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.jdp.SlingEnvironmentHelper;
import org.apache.sling.commons.jdp.RepositoryUtilTest;
import org.apache.sling.commons.jdp.impl.PropertyHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropertyHandlerTest {

	@Before
	public void setUp() throws Exception {
		SlingEnvironmentHelper.setUp();
	}

	@After
	public void tearDown() throws Exception {
		SlingEnvironmentHelper.tearDown();
	}

	@Test
	public void testCastProperty() throws Exception {
		String path = "/content/geometrixx/en/jcr:content";
		String prop = "cq:lastModified";
		Resource r = SlingEnvironmentHelper.getResource(null, null, path);
		assertTrue("Resource " + path + " was NULL", r != null);
		
		Property p = PropertyHandler.getProperty(r, prop);
		assertTrue("Property " + path+ " was NULL", p != null);
		Date d = PropertyHandler.castProperty(p, Date.class);
		assertTrue("Date was NULL", d != null);
	}
	
	@Test
	public void testCastPropertyArray() throws Exception {
		String path = "/etc/forms/referencedata/dropdown_values";
		String prop = "industryValues";
		
		Resource r = SlingEnvironmentHelper.getResource(null, null, path);
		assertTrue("Resource " + path + " was NULL", r != null);
		
		Property p = PropertyHandler.getProperty(r, prop);
		assertTrue("Property " + path+ " was NULL", p != null);
		
		String[] sa = PropertyHandler.castPropertyArray(p, String.class);
		assertTrue("Property array was empty", sa != null && sa.length > 0);
	}

}
