/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.LinkedList;
import java.util.List;

import org.python.pydev.parser.jython.ast.exprType;

public class TupleElement extends Value {
	
	private final LinkedList<Integer> indexes;

	public TupleElement(exprType expression, List<Integer> indexes) {
		super(expression);
		this.indexes = new LinkedList<Integer>(indexes);
	}
	
	public int getFirstIndex() {
		return indexes.getFirst();
	}
	
	public List<Integer> getRestOfIndexes() {
		return indexes.subList(1, indexes.size());
	}

	public int getIndexesCount() {
		return indexes.size();
	}
}
