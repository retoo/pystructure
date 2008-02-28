package org.pystructure.tests;

import ch.hsr.ifs.pystructure.PyStructure;

import junit.framework.TestCase;

public class PyStructureTest extends TestCase {

	private PyStructure obj;

	protected void setUp() throws Exception {
		obj = new PyStructure();
	}
	
	public void testAdd() {
		assertEquals(1 + 2, obj.add(1, 2));
		assertEquals(1 + 2, obj.add(2, 1));
	}
	
	public void testAdd2() {
		assertEquals(1 + 2, obj.add(2, 1));
	}

}
