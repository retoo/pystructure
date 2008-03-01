/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.goals.references;

import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.goals.base.PythonGoal;
import org.python.pydev.refactoring.typeinference.model.definitions.Method;

public class MethodReferencesGoal extends PythonGoal {

	private final Method method;

	public MethodReferencesGoal(PythonContext context, Method method) {
		super(context);
		this.method = method;
	}
	
	public Method getMethod() {
		return method;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof MethodReferencesGoal) {
			MethodReferencesGoal goal = (MethodReferencesGoal) obj;
			return method == goal.method;
		}
		return false;
	}

	public int hashCode() {
		if (method != null) {
			return method.hashCode();
		}
		return super.hashCode();
	}

	public String toString() {
		return "MethodReferencesGoal: "
		+ ((method != null) ? method.toString() : "null")
		+ " context: "
		+ ((getContext() != null) ? getContext().toString() : "null");
	}
}
