/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.references;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.PythonGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;

// TODO: Maybe implement with an AttributeReferencesGoal (with Module as the parent)?
public class ClassReferencesGoal extends PythonGoal {

	private final Class klass;

	public ClassReferencesGoal(ModuleContext context, Class klass) {
		super(context);
		this.klass = klass;
	}
	
	public Class getKlass() {
		return klass;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ClassReferencesGoal) {
			ClassReferencesGoal goal = (ClassReferencesGoal) obj;
			return klass == goal.klass;
		}
		return false;
	}

	public int hashCode() {
		if (klass != null) {
			return klass.hashCode();
		}
		return super.hashCode();
	}

	public String toString() {
		return "ClassReferencesGoal: "
		+ ((klass != null) ? klass.toString() : "null")
		+ " context: "
		+ ((getContext() != null) ? getContext().toString() : "null");
	}
}
