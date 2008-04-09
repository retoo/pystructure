/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.base;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;

public class AbstractGoal implements IGoal {

	protected final ModuleContext context;
	
	public AbstractGoal(ModuleContext context) {
		this.context = context;
	}
	
	public ModuleContext getContext() {
		return context;
	}

}
