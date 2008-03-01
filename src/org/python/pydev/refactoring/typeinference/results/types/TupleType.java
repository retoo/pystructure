/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.results.types;

import org.python.pydev.parser.jython.ast.Tuple;
import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;

public class TupleType implements IEvaluatedType {

	private Tuple tuple;
	
	public TupleType(Tuple tuple) {
		this.tuple = tuple;
	}
	
	public Tuple getTuple() {
		return tuple;
	}
	
	public String getTypeName() {
		return "tuple";
	}

	public boolean subtypeOf(IEvaluatedType type) {
		return false;
	}
	
	// TODO: equals & hashCode
}
