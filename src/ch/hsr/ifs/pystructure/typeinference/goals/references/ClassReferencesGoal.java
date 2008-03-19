/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.references;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.AbstractGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.results.references.ClassReference;

// TODO: Maybe implement with an AttributeReferencesGoal (with Module as the parent)?
public class ClassReferencesGoal extends AbstractGoal {

	private final Class klass;
	public final List<ClassReference> references;

	public ClassReferencesGoal(ModuleContext context, Class klass) {
		super(context);
		this.klass = klass;
		this.references = new ArrayList<ClassReference>();
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
