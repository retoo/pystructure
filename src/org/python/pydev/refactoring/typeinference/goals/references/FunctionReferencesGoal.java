/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.goals.references;

import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.goals.base.PythonGoal;
import org.python.pydev.refactoring.typeinference.model.definitions.Function;

public class FunctionReferencesGoal extends PythonGoal {

	private final Function function;

	public FunctionReferencesGoal(PythonContext context, Function function) {
		super(context);
		this.function = function;
	}
	
	public Function getFunction() {
		return function;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof FunctionReferencesGoal) {
			FunctionReferencesGoal goal = (FunctionReferencesGoal) obj;
			return function == goal.function;
		}
		return false;
	}

	public int hashCode() {
		if (function != null) {
			return function.hashCode();
		}
		return super.hashCode();
	}

	public String toString() {
		return "FunctionReferencesGoal: "
		+ ((function != null) ? function.toString() : "null")
		+ " context: "
		+ ((getContext() != null) ? getContext().toString() : "null");
	}
}
