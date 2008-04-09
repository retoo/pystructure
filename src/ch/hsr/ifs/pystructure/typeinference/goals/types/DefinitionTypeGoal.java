/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.types;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;

public class DefinitionTypeGoal extends AbstractTypeGoal {

	private final Definition definition;

	public DefinitionTypeGoal(ModuleContext context, Definition definition) {
		super(context);
		if (definition == null) {
			throw new RuntimeException("definition has to be set");
		}
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
		+ " context: " + context;
	}
}
