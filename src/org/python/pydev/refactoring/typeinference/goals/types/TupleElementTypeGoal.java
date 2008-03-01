/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.goals.types;

import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.model.definitions.TupleElement;

public class TupleElementTypeGoal extends PythonTypeGoal {

	private final TupleElement element;
	
	public TupleElementTypeGoal(PythonContext context, TupleElement element) {
		super(context);
		this.element = element;
	}
	
	public TupleElement getTupleElement() {
		return element;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof TupleElementTypeGoal) {
			TupleElementTypeGoal goal = (TupleElementTypeGoal) obj;
			return element == goal.element;
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (element != null) {
			return element.hashCode();
		}
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "TupleElementTypeGoal: "
				+ ((element != null) ? element.toString() : "null")
				+ " context: "
				+ ((context != null) ? context.toString() : "null");
	}
}
