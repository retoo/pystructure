/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.goals.base;

import org.python.pydev.refactoring.typeinference.contexts.PythonContext;

public class PythonGoal implements IPythonGoal {

	private final PythonContext context;
	
	public PythonGoal(PythonContext context) {
		this.context = context;
	}
	
	public PythonContext getContext() {
		return context;
	}

}
