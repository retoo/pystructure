/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.goals.types;

import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.dltk.goals.AbstractTypeGoal;
import org.python.pydev.refactoring.typeinference.goals.base.IPythonGoal;

public class PythonTypeGoal extends AbstractTypeGoal implements IPythonGoal {

	public PythonTypeGoal(PythonContext context) {
		super(context);
	}
	
	@Override
	public PythonContext getContext() {
		// Our constructor only allows PythonContext, so this cast should be ok.
		return (PythonContext) super.getContext();
	}

}
