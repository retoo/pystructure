/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.evaluators;

import org.python.pydev.refactoring.typeinference.dltk.evaluators.GoalEvaluator;
import org.python.pydev.refactoring.typeinference.goals.base.IPythonGoal;

/**
 * Base class for all the Python evaluators. It evaluates an {@link IPythonGoal}.
 */
public abstract class PythonEvaluator extends GoalEvaluator {

	public PythonEvaluator(IPythonGoal goal) {
		super(goal);
	}
	
	@Override
	public IPythonGoal getGoal() {
		// Our constructor only allows IPythonGoal, so this cast should be ok.
		return (IPythonGoal) super.getGoal();
	}

}
