/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.base;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;

public class PythonGoal implements IPythonGoal {

	private final ModuleContext context;
	
	public PythonGoal(ModuleContext context) {
		this.context = context;
	}
	
	public ModuleContext getContext() {
		return context;
	}

}
