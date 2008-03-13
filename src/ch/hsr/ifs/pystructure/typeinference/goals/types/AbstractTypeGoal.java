/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.types;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IPythonGoal;

public abstract class AbstractTypeGoal implements IPythonGoal {

	protected final ModuleContext context;
	
	public AbstractTypeGoal(ModuleContext context) {
		this.context = context;
	}
	
	public ModuleContext getContext() {
		return context;
	}

}
