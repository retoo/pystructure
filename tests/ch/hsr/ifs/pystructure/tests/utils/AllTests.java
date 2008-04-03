/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.tests.utils;

import junit.framework.Test;
import junit.framework.TestSuite;

public final class AllTests {

	private AllTests() {
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for utils");
		//$JUnit-BEGIN$
		suite.addTestSuite(StringUtilsTest.class);
		suite.addTestSuite(TestUtilsTest.class);
		suite.addTestSuite(FileUtilsTest.class);
		//$JUnit-END$
		return suite;
	}

}
