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

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public final class TestUtils {

	private static final Pattern CURSOR_PATTERN = Pattern.compile("##\\|");
	private static final String CURSOR = "##|";

	private TestUtils() { }

	public static final class Marker {

		public enum Type { TYPE, MRO } 

		public final int beginLine;
		public final String expr;
		public final String type;
		public final Type markerType;

		public Marker(Type markerType, int beginLine, String expr, String type) {
			this.markerType = markerType;
			this.beginLine = beginLine;
			this.expr = expr;
			this.type = type;
		}

	}

	public static List<Marker> getMarkers(String sourceCode) {
		List<Marker> markers = new LinkedList<Marker>();

		String[] lines = sourceCode.split("\n");

		int beginLine = 0;

		for (String line : lines) {
			beginLine += 1;

			line = line.trim();
			int pos = line.indexOf("##");
			if (pos < 0) {
				continue;
			}

			StringTokenizer tok = new StringTokenizer(line.substring(pos + 2));
			String test = tok.nextToken();

			if (test.equals("exit")) {
				break;
			} else { 
				String type = tok.nextToken();
				String expr = line.substring(0, line.indexOf("##")).trim();
				
				if (test.equals("type")) {
					markers.add(new Marker(Marker.Type.TYPE, beginLine, expr, type));
				} else if (test.equals("mro")) {
					markers.add(new Marker(Marker.Type.MRO, beginLine, expr, type));				
				} else {
					throw new RuntimeException("Unexpected marker type '" + test + "' at line " + beginLine);
				}
			}
		}

		return markers;
	}

	public static final class Cursors {

		public final String text;
		public final List<Integer> positions;

		public Cursors(String text, List<Integer> positions) {
			this.text = text;
			this.positions = positions;
		}

	}

	public static Cursors findCursors(String input) {
		List<Integer> positions = new LinkedList<Integer>();
		String text = input;

		while (true) {
			int index = text.indexOf(CURSOR);

			if (index < 0) {
				break;
			}

			positions.add(index);
			text = CURSOR_PATTERN.matcher(text).replaceFirst("");
		}

		return new Cursors(text, positions);
	}

}
