/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.tests.typeinference;

import junit.framework.TestCase;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.Expr;
import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.parser.jython.ast.NameTok;

import ch.hsr.ifs.pystructure.parser.Parser;

public class ExpressionAtPositionVisitorTest extends TestCase {
	public void testName() throws Exception {
		String source = "name";
		Expr expression = getExpression(source, "name", 1);
		assertNotNull(expression);
		Name name = (Name) expression.value;
		assertEquals("name", name.id);
	}
	
	public void testName2() throws Exception {
		String source = "name1\nname2";
		Expr expression = getExpression(source, "name2", 2);
		assertNotNull(expression);
		Name name = (Name) expression.value;
		assertEquals("name2", name.id);
	}
	
	public void testAttribute() throws Exception {
		String source = "name\nname.attribute";
		Expr expression = getExpression(source, "name.attribute", 2);
		assertNotNull(expression);
		Attribute attribute = (Attribute) expression.value;
		NameTok name = (NameTok) attribute.attr;
		assertEquals("attribute", name.id);
	}

	private Expr getExpression(String source, String wantedExpression, int line) throws Exception {
		SimpleNode node = Parser.parse(source);
		ExpressionAtPositionVisitor visitor = new ExpressionAtPositionVisitor(wantedExpression, line);
		visitor.traverse(node);
		Expr expression = visitor.getExpression();
		return expression;
	}
}
