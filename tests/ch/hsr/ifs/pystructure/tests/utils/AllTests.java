/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
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
		suite.addTestSuite(NodeUtilsTest.class);
		//$JUnit-END$
		return suite;
	}

}
