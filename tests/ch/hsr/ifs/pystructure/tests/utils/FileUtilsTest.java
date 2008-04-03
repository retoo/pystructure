/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
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
