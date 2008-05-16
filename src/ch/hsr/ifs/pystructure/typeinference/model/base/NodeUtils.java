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

import java.util.HashMap;
import java.util.Map;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.Index;
import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.parser.jython.ast.NameTok;
import org.python.pydev.parser.jython.ast.NameTokType;
import org.python.pydev.parser.jython.ast.Num;
import org.python.pydev.parser.jython.ast.Subscript;
import org.python.pydev.parser.jython.ast.Tuple;
import org.python.pydev.parser.jython.ast.UnaryOp;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.jython.ast.expr_contextType;
import org.python.pydev.parser.jython.ast.name_contextType;
import org.python.pydev.parser.jython.ast.num_typeType;
import org.python.pydev.parser.jython.ast.unaryopType;

public final class NodeUtils {

	private NodeUtils() {
	}

	public static String nodePosition(SimpleNode node) {
		return 
		node != null
		? "(L" + node.beginLine + " C" + node.beginColumn + ")"
				: "";
	}

	/**
	 * Returns the string id of the name token.
	 */
	public static String getId(NameTokType name) {
		return ((NameTok) name).id;
	}
	
	/**
	 * Returns the integer value of the Num node if it is one or null otherwise.
	 */
	public static Integer extractInteger(Num num) {
		switch (num.type) {
		case num_typeType.Long:
		case num_typeType.Float:
		case num_typeType.Comp:
			return null;
		case num_typeType.Int:
		case num_typeType.Oct:
		case num_typeType.Hex:
		default:
			return (Integer) num.n;
		}
	}
	
	/**
	 * Returns the integer value of the expression if it is an integer literal
	 * (positive or negative) or null otherwise.
	 */
	public static Integer extractInteger(exprType literalNumber) {
		if (literalNumber instanceof Num) {
			return extractInteger((Num) literalNumber);
		} else if (literalNumber instanceof UnaryOp) {
			UnaryOp unaryOp = (UnaryOp) literalNumber;
			if (unaryOp.op == unaryopType.USub && unaryOp.operand instanceof Num) {
				return -1 * extractInteger((Num) unaryOp.operand);
			}
		}
		
		/* Couldn't extract an integer, so return null */
		return null;
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
	 * Creates a call node for a function call.
	 */
	public static Call createFunctionCall(String function, exprType... arguments) {
		Name name = new Name(function, expr_contextType.Load);
		Call call = new Call(name, arguments, null, null, null);
		name.parent = call;
		return call;
	}

	/**
	 * Create a call node for a method call from the specified receiver
	 * expression, method name and arguments: receiver.method(arg1, arg2)
	 */
	public static Call createMethodCall(exprType receiver, String method, exprType... arguments) {
		NameTok methodTok = new NameTok(method, name_contextType.Attrib);
		Attribute func = new Attribute(receiver, methodTok, expr_contextType.Load);
		methodTok.parent = func;
		Call call = new Call(func, arguments, null, null, null);
		func.parent = call;
		return call;
	}

	/**
	 * Creates multiple single assignments from a target which is possibly a
	 * (nested) tuple. Example:
	 * 
	 * a, (b, c[i]) = 1, (2, 3)
	 * 
	 * "a, (b, c[i])" is the target and "1, (2, 3)" the value. The result is
	 * the following map:
	 * 
	 * a    → (1, (2, 3))[0]
	 * b    → (1, (2, 3))[1][0]
	 * c[i] → (1, (2, 3))[1][1]
	 * 
	 * @param target assignment target, may be a tuple
	 * @param value assignment value
	 * @return a map from single targets to unpacked values
	 */
	public static Map<exprType, exprType> createTupleElementAssignments(exprType target, exprType value) {
		Map<exprType, exprType> values = new HashMap<exprType, exprType>();
		createTupleElementAssignments(target, value, values);
		return values;
	}

	private static void createTupleElementAssignments(exprType target, exprType value, Map<exprType, exprType> values) {
		if (target instanceof Tuple) {
			Tuple tuple = (Tuple) target;
			for (int i = 0; i < tuple.elts.length; i++) {
				exprType element = tuple.elts[i];
				Index index = NodeUtils.createIndex(i);
				Subscript subscript = new Subscript(value, index, expr_contextType.Load);
				createTupleElementAssignments(element, subscript, values);
			}
		} else {
			values.put(target, value);
		}
	}

	/**
	 * Create an Index node with the specified number as the index.
	 */
	public static Index createIndex(int number) {
		Num num = new Num(Integer.valueOf(number), num_typeType.Int, String.valueOf(number));
		return new Index(num);
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
