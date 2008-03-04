/* 
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
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

	public static Module parse(String source) throws ParseException {
		/* Convert the string into an char array and add a \n at the end
		 * NOTE: the a final newline is necessary for the parser */
		int count = source.length();
		char sourceChars[] = new char[count + 1];
		source.getChars(0, count, sourceChars, 0);
		sourceChars[count] = '\n';

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
