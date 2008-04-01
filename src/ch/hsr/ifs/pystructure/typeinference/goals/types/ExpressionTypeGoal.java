/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.types;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;

public class ExpressionTypeGoal extends AbstractTypeGoal {

	private final SimpleNode expression;
	
	public ExpressionTypeGoal(ModuleContext context, SimpleNode expression) {
		super(context);
		if (expression == null) {
			throw new RuntimeException("expression must not be null");
		}
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
				+ expression
				+ NodeUtils.nodePosition(expression)
				+ " context: " + context;
	}
}
