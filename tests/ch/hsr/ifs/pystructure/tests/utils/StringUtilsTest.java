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

import java.util.LinkedList;

import junit.framework.TestCase;
import ch.hsr.ifs.pystructure.utils.StringUtils;

public class StringUtilsTest extends TestCase {

	public void testJoin() {
		assertEquals("a.b.c", StringUtils.join('.', l("a", "b", "c")));
		assertEquals("", StringUtils.join('.', l()));
		assertEquals("a", StringUtils.join('.', l("a")));
	}

	public void testCapitalize() {
		assertEquals("Changed", StringUtils.capitalize("changed"));
		assertEquals("Unchanged", StringUtils.capitalize("Unchanged"));
		assertEquals("TwoWords", StringUtils.capitalize("twoWords"));
		assertEquals("A", StringUtils.capitalize("a"));
		assertEquals("", StringUtils.capitalize(""));
	}

	public void testStripParts() {
		String input = "foo/bar/baz/xyz.py";
		
		assertEquals("foo/bar/baz",			StringUtils.stripParts(input, 1));
		assertEquals("foo/bar",				StringUtils.stripParts(input, 2));
		assertEquals("foo",					StringUtils.stripParts(input, 3));
		assertEquals("",					StringUtils.stripParts(input, 4));
	}
	
	public void testMultiply() {
		assertEquals("lololo", StringUtils.multiply(3, "lo"));
	}

	private static LinkedList<String> l(String... array) {
		LinkedList<String> list = new LinkedList<String>();
		for (String item : array) {
			list.add(item);
		}

		return list;
	}

}
