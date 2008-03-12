/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.types;

import ch.hsr.ifs.pystructure.typeinference.contexts.PythonContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IPythonGoal;

public abstract class PythonTypeGoal implements IPythonGoal {

	protected final PythonContext context;
	
	public PythonTypeGoal(PythonContext context) {
		this.context = context;
	}
	
	public PythonContext getContext() {
		return context;
	}

}
