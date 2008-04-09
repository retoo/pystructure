/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.references;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;

public class FunctionReferencesGoal extends CallableGoal {

	private final Function function;

	public FunctionReferencesGoal(ModuleContext context, Function function) {
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
