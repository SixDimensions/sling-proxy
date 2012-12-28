package org.apache.sling.commons.reflection;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReflectTestCase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInterfaces() {
		assertTrue("Failed Generic Type Test", testClass(MyInterface.class));
	}
	
	private boolean testClass(Class<? extends SlingBean> t) {
		return true;
	}
	
	interface MyInterface extends SlingBean, Interface001, Interface002 {
		String getId();
	}
	
	interface Interface001 {
		String getOne();
	}
	
	interface Interface002 {
		String getTwo();
	}
	
	interface SlingBean {
		String getName();
	}
}
