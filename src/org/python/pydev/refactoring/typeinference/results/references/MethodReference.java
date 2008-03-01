/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.results.references;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.refactoring.typeinference.model.definitions.Argument;
import org.python.pydev.refactoring.typeinference.model.definitions.Method;

public class MethodReference extends FunctionReference {
	private boolean firstArgumentIsImplicit;

	public MethodReference(Method method, SimpleNode node) {
		this(method, node, true);
	}
	
	public MethodReference(Method method, SimpleNode node, boolean firstArgumentIsImplicit) {
		super(method, node);
		this.firstArgumentIsImplicit = firstArgumentIsImplicit;
	}

	@Override
	public exprType getArgumentExpression(Argument argument) {
		return super.getArgumentExpression(argument, firstArgumentIsImplicit);
	}
}
