/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.types;

import ch.hsr.ifs.pystructure.typeinference.contexts.PythonContext;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;

public class ReturnTypeGoal extends PythonTypeGoal {

	private Function function;

	public ReturnTypeGoal(PythonContext context, Function function) {
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
		if (obj instanceof ReturnTypeGoal) {
			ReturnTypeGoal goal = (ReturnTypeGoal) obj;
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
		return "ExpressionTypeGoal: "
		+ ((function != null) ? function.toString() : "null")
		+ " context: "
		+ ((context != null) ? context.toString() : "null");
	}
}
