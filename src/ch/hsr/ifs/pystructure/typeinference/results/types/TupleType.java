/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.types;

import org.python.pydev.parser.jython.ast.Tuple;

public class TupleType extends AbstractType {

	private Tuple tuple;
	
	public TupleType(Tuple tuple) {
		super("tuple");
		this.tuple = tuple;
	}
	
	public Tuple getTuple() {
		return tuple;
	}
	
}
