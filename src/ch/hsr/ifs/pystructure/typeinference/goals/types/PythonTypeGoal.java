/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.types;

import ch.hsr.ifs.pystructure.typeinference.contexts.PythonContext;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IPythonGoal;

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
