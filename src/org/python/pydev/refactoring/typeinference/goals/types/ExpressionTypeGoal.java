/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.goals.types;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.refactoring.typeinference.contexts.PythonContext;

public class ExpressionTypeGoal extends PythonTypeGoal {

	private final SimpleNode expression;
	
	public ExpressionTypeGoal(PythonContext context, SimpleNode expression) {
		super(context);
		this.expression = expression;
	}

	public SimpleNode getExpression() {
		return expression;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal goal = (ExpressionTypeGoal) obj;
			return expression == goal.expression;
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (expression != null) {
			return expression.hashCode();
		}
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "ExpressionTypeGoal: "
				+ ((expression != null) ? expression.toString() : "null")
				+ " context: "
				+ ((context != null) ? context.toString() : "null");
	}
}
