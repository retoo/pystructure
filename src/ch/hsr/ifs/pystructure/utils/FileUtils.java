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

package ch.hsr.ifs.pystructure.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class FileUtils {
	private static final int BUFFER_SIZE = 1024;
	
	private FileUtils() { }

	/**
	 * Read the whole content of a file.
	 * 
	 * @param file
	 * @return the file's content
	 * @throws IOException
	 */
	public static String read(File file) throws IOException {
		return read(new FileReader(file));
	}
	
	
	public static String read(InputStream inputStream) throws IOException {
		return read(new InputStreamReader(inputStream));
	}

	/**
	 * Reads everything from a reader and returns it as a string. 
	 * 
	 * @param in reader
	 * @return String containing all the reader data
	 * @throws IOException
	 */
	private static String read(Reader in) throws IOException {
		BufferedReader reader = new BufferedReader(in);
		
		try {
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[BUFFER_SIZE];
			int len;
			while ((len = reader.read(buffer, 0, BUFFER_SIZE)) != -1) {
				builder.append(buffer, 0, len);
			}
			return builder.toString();
		} finally {
			reader.close();
		}
	}

	/**
	 * Strips away the extension of the given filename.
	 * 
	 * @param filename
	 * @return
	 */
	public static String stripExtension(String filename) {
		int point = filename.lastIndexOf(".");
		
		return point >= 0 
				? filename.substring(0, point)
				: filename;
	}

	public static void mkdir(File dir) {
		if (!dir.exists()) {
			boolean res = dir.mkdir();
			
			if (!res) {
				throw new RuntimeException("Unable to create directory " + dir);
			}
		}
	}

}
