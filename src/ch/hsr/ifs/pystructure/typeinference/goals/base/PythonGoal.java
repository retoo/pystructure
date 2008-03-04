/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.base;

import ch.hsr.ifs.pystructure.typeinference.contexts.PythonContext;

public class PythonGoal implements IPythonGoal {

	private final PythonContext context;
	
	public PythonGoal(PythonContext context) {
		this.context = context;
	}
	
	public PythonContext getContext() {
		return context;
	}

}
