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

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import ch.hsr.ifs.pystructure.utils.FileUtils;

public class FileUtilsTest extends TestCase {

	public void testRead() throws IOException {
		File file = new File("tests/ch/hsr/ifs/pystructure/tests/utils/smallfile.txt");
		String contents = FileUtils.read(file);
		assertEquals("This\nis\na\nsmall\ntext\nfile.", contents);
	}
	
	public void testReadNonexistent() {
		File file = new File("thisfiledoesnotexistsowetrytoreadittoseeifanioexceptionisthrown");
		try {
			FileUtils.read(file);
			fail("IOException expected (tongue twister)");
		} catch (IOException e) {
		}
	}
	
	public void testStripExtension() {
		assertEquals("foo", FileUtils.stripExtension("foo.bar"));
		assertEquals("foo.bar", FileUtils.stripExtension("foo.bar.baz"));
		assertEquals("/path/to/file", FileUtils.stripExtension("/path/to/file.txt"));
		assertEquals("", FileUtils.stripExtension(".foo"));
		assertEquals(".foo", FileUtils.stripExtension(".foo.foo"));
		assertEquals("", FileUtils.stripExtension("."));
		assertEquals(".", FileUtils.stripExtension(".."));
	}
	
}
