/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.tests.typeinference;

import org.python.pydev.parser.jython.Visitor;
import org.python.pydev.parser.jython.ast.Expr;

public class ExpressionAtPositionVisitor extends Visitor {
	private int beginLine;
	
	private Expr expression;
	
	public ExpressionAtPositionVisitor(String wantedExpression, int beginLine) {
		this.beginLine = beginLine;
	}

	@Override
	public Object visitExpr(Expr node) throws Exception {
		// There seems to be a bug with Expr.beginColumn, it's actually
		// endColumn. So we can't use it, we have to hope that the wanted
		// expression is the only one on that line.
		if (node.beginLine == beginLine) {
			expression = node;
		}
		return null;
	}
	
	public Expr getExpression() {
		return expression;
	}
}
