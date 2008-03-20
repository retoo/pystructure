/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.types;

import org.python.pydev.parser.jython.ast.Tuple;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;

public class TupleType implements IType {

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

	public boolean subtypeOf(IType type) {
		return false;
	}
	
	// TODO: equals & hashCode
}
