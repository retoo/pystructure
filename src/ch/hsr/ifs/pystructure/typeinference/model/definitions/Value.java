/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.exprType;

public class Value {
	
	private exprType expression;
	
	public Value(exprType expression) {
		this.expression = expression;
	}
	
	public exprType getExpression() {
		return expression;
	}
}
