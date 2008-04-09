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

package ch.hsr.ifs.pystructure.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class LineIterator implements Iterable<String>, Iterator<String> {

	private BufferedReader bufferedReader;
	private String nextLine;

	public LineIterator(Reader reader) {
		bufferedReader = new BufferedReader(reader);
		
		readNextLine();
	}

	private void readNextLine() {
		try {
			nextLine = bufferedReader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Iterator<String> iterator() {
		return this;
	}

	public boolean hasNext() {
		return nextLine != null;
	}

	public String next() {
		String currentLine = nextLine;
		readNextLine();
		return currentLine;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
