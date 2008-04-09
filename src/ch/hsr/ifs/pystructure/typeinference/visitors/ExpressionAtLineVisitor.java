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

package ch.hsr.ifs.pystructure.typeinference.visitors;

import org.python.pydev.parser.jython.Visitor;
import org.python.pydev.parser.jython.ast.Expr;

/**
 * Visitor which finds an expression at a specified line number.
 */
public class ExpressionAtLineVisitor extends Visitor {
	private int beginLine;
	
	private Expr expression;
	
	public ExpressionAtLineVisitor(int beginLine) {
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
