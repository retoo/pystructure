package ch.hsr.ifs.pystructure.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.pystructure.tests");
		//$JUnit-BEGIN$
		suite.addTest(ch.hsr.ifs.pystructure.tests.typeinference.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
