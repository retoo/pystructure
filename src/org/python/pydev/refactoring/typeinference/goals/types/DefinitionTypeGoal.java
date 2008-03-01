/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.goals.types;

import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.model.definitions.Definition;

public class DefinitionTypeGoal extends PythonTypeGoal {

	private final Definition definition;

	public DefinitionTypeGoal(PythonContext context, Definition definition) {
		super(context);
		this.definition = definition;
	}

	public Definition getDefinition() {
		return definition;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof DefinitionTypeGoal) {
			DefinitionTypeGoal goal = (DefinitionTypeGoal) obj;
			return definition == goal.definition;
		}
		return false;
	}

	public int hashCode() {
		if (definition != null) {
			return definition.hashCode();
		}
		return super.hashCode();
	}

	public String toString() {
		return "DefinitionTypeGoal: "
		+ ((definition != null) ? definition.toString() : "null")
		+ " context: "
		+ ((getContext() != null) ? getContext().toString() : "null");
	}
}
