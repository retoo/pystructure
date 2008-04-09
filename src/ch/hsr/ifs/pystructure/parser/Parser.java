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

package ch.hsr.ifs.pystructure.parser;

import org.python.pydev.parser.CompilerAPI;
import org.python.pydev.parser.IGrammar;
import org.python.pydev.parser.grammar25.PythonGrammar25;
import org.python.pydev.parser.jython.CharStream;
import org.python.pydev.parser.jython.FastCharStream;
import org.python.pydev.parser.jython.IParserHost;
import org.python.pydev.parser.jython.ParseException;
import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.TokenMgrError;
import org.python.pydev.parser.jython.ast.Module;

public final class Parser {
	
	private Parser() {
	}

	public static Module parse(String source) throws ParseException {
		/* Convert the string into an char array and add a \n at the end
		 * NOTE: the a final newline is necessary for the parser */
		int count = source.length();
		char[] sourceChars = new char[count + 5];
		source.getChars(0, count, sourceChars, 0);
		sourceChars[count + 0] = '\n';
		/* yap, I agree, a very nice way to do that, isnt it? This fixes a 
		 * strange bug in the PyDev Jython parser, it appears to dislike comments at the 
		 * end of the file */ 
		sourceChars[count + 1] = 'p';
		sourceChars[count + 2] = 'a';
		sourceChars[count + 3] = 's';
		sourceChars[count + 4] = 's';
		

		CharStream sourceStream = new FastCharStream(sourceChars);

		IParserHost host = new CompilerAPI();
		IGrammar grammar = new PythonGrammar25(sourceStream, host);

		try {
			SimpleNode rootNode = grammar.file_input();
			return (Module) rootNode;
		} catch (TokenMgrError e) {
			throw new ParseException(e.getMessage());
		}
	}

}
