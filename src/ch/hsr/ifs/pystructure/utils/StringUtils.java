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
import java.util.Locale;
import java.util.regex.Pattern;

public final class StringUtils {

	private static final Pattern DOT = Pattern.compile("\\.");
	
	private StringUtils() { }
	
	/**
	 * Joins the supplied parts together, using dots as a glue
	 * 
	 * @param parts parts to join
	 * @return string containing joined parts 
	 */
	public static String join(char delimiter, Iterable<?> parts) {
		StringBuilder sb = new StringBuilder();
		
		boolean first = true;
		for (Object part : parts) {
			
			if (first) {
				first = false;
			} else {
				sb.append(delimiter);
			}
			sb.append(part);
		}
		
		return sb.toString();
	}

	/**
	 * Returns the string with the first character in upper case.
	 * 
	 * @param string "example string"
	 * @return "Example string"
	 */
	public static String capitalize(String string) {
		if (string.length() == 0) {
			return string; 
		} else {
			return string.substring(0, 1).toUpperCase(Locale.ENGLISH) + string.substring(1);
		}
	}

	/**
	 * Takes the '/' separated string and removes level parts of it
	 * 
	 * Example:
	 * input is foo/bar/baz
	 * 
	 * with level 1: "foo/bar"
	 * with level 2: "foo"
	 * with level 3  ""
	 * 
	 * @param string
	 * @param level level
	 * @return truncated string
	 */
	public static String stripParts(String string, int level) {
		/* level times remove /.*$ */
		for (int i = 0; i < level; i++) {
			int index = string.lastIndexOf('/');
			
			if (index < 0) {
				/* no slashhes.. very well */
				return "";
			}
			
			string = string.substring(0, index);
		}
		return string;
	}

	public static LinkedList<String> dotSplit(String string) {
		return split(string, DOT);
	}

	public static LinkedList<String> split(String string, Pattern re) {
		LinkedList<String> parts = new LinkedList<String>();
		for (String part : re.split(string)) {
			parts.add(part);
		}
		return parts;
	}
	
	public static String multiply(int times, String str) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < times; i++) {
			sb.append(str);
		}
		
		return sb.toString();
	}

}
