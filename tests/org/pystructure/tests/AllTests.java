package org.pystructure.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.pystructure.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(PyStructureTest.class);
		//$JUnit-END$
		return suite;
	}

}
