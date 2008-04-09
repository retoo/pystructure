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

import java.util.List;

import ch.hsr.ifs.pystructure.utils.TestUtils;
import ch.hsr.ifs.pystructure.utils.TestUtils.Cursors;
import ch.hsr.ifs.pystructure.utils.TestUtils.Marker;

import junit.framework.TestCase;

public class TestUtilsTest extends TestCase {

	public void testGetMarkers() {
		List<Marker> markers = TestUtils.getMarkers("x = 10 ## type int\ny = 'hello' ## type str");
		
		assertEquals(2, markers.size());
		
		Marker intMarker = markers.get(0);
		
		assertEquals("x = 10", 			intMarker.expr);
		assertEquals("int", 			intMarker.type);
		assertEquals(1, 				intMarker.beginLine);
		
		Marker strMarker = markers.get(1);
		
		assertEquals("y = 'hello'", 	strMarker.expr);
		assertEquals("str", 			strMarker.type);
		assertEquals(2, 				strMarker.beginLine);
	}

	public void testFindCursors() {
		Cursors findCursors = TestUtils.findCursors("##|x##|x##|x##|x##|");
		
		assertEquals("xxxx", findCursors.text);
		assertEquals(5, findCursors.positions.size());
		for (int i = 0; i < 5; i++) {
			assertEquals((Integer) i, findCursors.positions.get(i));
		}
	}

}
