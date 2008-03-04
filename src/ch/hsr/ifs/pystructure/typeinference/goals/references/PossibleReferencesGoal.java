/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.references;

import ch.hsr.ifs.pystructure.typeinference.contexts.PythonContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.PythonGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

public class PossibleReferencesGoal extends PythonGoal {

	private NameAdapter name;
	
	public PossibleReferencesGoal(PythonContext context, NameAdapter name) {
		super(context);
		this.name = name;
	}
	
	public NameAdapter getName() {
		return name;
	}

	@Override
	public String toString() {
		return "PossibleReferencesGoal: "
		+ ((name != null) ? name.toString() : "null")
		+ " context: "
		+ ((getContext() != null) ? getContext().toString() : "null");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PossibleReferencesGoal other = (PossibleReferencesGoal) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
