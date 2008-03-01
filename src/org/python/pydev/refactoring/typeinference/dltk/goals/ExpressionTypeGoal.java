/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.python.pydev.refactoring.typeinference.dltk.goals;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.refactoring.typeinference.dltk.contexts.IContext;

public class ExpressionTypeGoal extends AbstractTypeGoal {

	private final SimpleNode expression;

	public ExpressionTypeGoal(IContext context, SimpleNode expression) {
		super(context);
		this.expression = expression;
	}

	public SimpleNode getExpression() {
		return expression;
	}

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

	public int hashCode() {
		if (expression != null) {
			return expression.hashCode();
		}
		return super.hashCode();
	}

	public String toString() {
		return "ExpressionTypeGoal: "
				+ ((expression != null) ? expression.toString() : "null")
				+ " context: "
				+ ((context != null) ? context.toString() : "null");
	}

}
