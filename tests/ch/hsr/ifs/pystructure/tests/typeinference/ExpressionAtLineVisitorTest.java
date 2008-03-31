/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.tests.typeinference;

import junit.framework.TestCase;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.Expr;
import org.python.pydev.parser.jython.ast.Name;

import ch.hsr.ifs.pystructure.parser.Parser;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.visitors.ExpressionAtLineVisitor;

public class ExpressionAtLineVisitorTest extends TestCase {

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
		assertEquals("attribute", NodeUtils.getId(attribute.attr));
	}

	private Expr getExpression(String source, String wantedExpression, int line) throws Exception {
		SimpleNode node = Parser.parse(source);
		ExpressionAtLineVisitor visitor = new ExpressionAtLineVisitor(line);
		visitor.traverse(node);
		Expr expression = visitor.getExpression();
		return expression;
	}

}
