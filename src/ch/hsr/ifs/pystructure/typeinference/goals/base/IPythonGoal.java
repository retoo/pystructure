/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.base;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;

public interface IPythonGoal extends IGoal {

	ModuleContext getContext();

}
