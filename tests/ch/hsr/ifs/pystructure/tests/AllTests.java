/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

package ch.hsr.ifs.pystructure.tests;

import ch.hsr.ifs.pystructure.tests.export.ExporterTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public final class AllTests {
	
	private AllTests() {
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.pystructure.tests");
		//$JUnit-BEGIN$
		suite.addTest(ch.hsr.ifs.pystructure.tests.typeinference.AllTests.suite());
		suite.addTest(ch.hsr.ifs.pystructure.tests.utils.AllTests.suite());
		suite.addTestSuite(ExporterTest.class);
		//$JUnit-END$
		return suite;
	}

}
