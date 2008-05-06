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

package ch.hsr.ifs.pystructure.typeinference.model.base;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.NameTok;
import org.python.pydev.parser.jython.ast.NameTokType;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.jython.ast.expr_contextType;
import org.python.pydev.parser.jython.ast.name_contextType;

public final class NodeUtils {

	private NodeUtils() {
	}

	public static String nodePosition(SimpleNode node) {
		return 
		node != null
		? "(L" + node.beginLine + " C" + node.beginColumn + ")"
				: "";
	}

	public static String getId(NameTokType name) {
		return ((NameTok) name).id;
	}
	
	/**
	 * Returns the Call node of the passed func expression if there is one.
	 * 
	 * For example in "module.func(arg)", the passed function would be
	 * "module.func" and the result the call node of that function call.
	 */
	public static Call getCallForFunc(exprType expression) {
		if (expression.parent instanceof Call) {
			Call call = (Call) expression.parent;
			if (call.func == expression) {
				return call;
			}
		}
		
		return null;
	}
	
	/**
	 * Create a call node for a method call from the specified receiver
	 * expression, method name and arguments: receiver.method(arg1, arg2)
	 */
	public static Call createMethodCall(exprType receiver, String method, exprType... arguments) {
		NameTok methodTok = new NameTok(method, name_contextType.Attrib);
		Attribute func = new Attribute(receiver, methodTok, expr_contextType.Load);
		return new Call(func, arguments, null, null, null);
	}
	
	/**
	 * Pretty-print the given AST node to stdout.
	 *
	 * @param node
	 */
	public static void prettyPrint(SimpleNode node) {
		System.out.println(getPrettyPrinted(node));
	}

	/**
	 * Returns a pretty-printed given ast
	 *
	 * @param node top-level node
	 * @return string containing the human-readable ast
	 */
	public static String getPrettyPrinted(SimpleNode node) {
		String str = node.toString();

		StringBuilder out = new StringBuilder();

		int level = 0;
		boolean skipSpace = false;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if (c == '[') {
				out.append("");
				out.append("\n");
				level++;
				printIdent(out, level);
			} else if (c == ',') {
				out.append(c);
				out.append("\n");
				printIdent(out, level);
				skipSpace = true;

			} else if (c == ' ' && skipSpace) {
				skipSpace = false;
			} else if (c == ']') {
				level--;
				out.append("\n");
				printIdent(out, level);
				//out.append("]");
			} else if (c == '=') {
				out.append(" = ");
			} else {
				out.append(c);
			}
		}


		return out.toString();
	}

	private static void printIdent(StringBuilder out, int level) {
		for (int i = 0; i < level; i++) {
			out.append("|   ");
		}
	}

}
