/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators;

import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

/**
 * Base class for all the Python evaluators. It evaluates an {@link IGoal}.
 */
public abstract class PythonEvaluator extends GoalEvaluator {

	public PythonEvaluator(IGoal goal) {
		super(goal);
	}
	
	@Override
	public IGoal getGoal() {
		// Our constructor only allows IPythonGoal, so this cast should be ok.
		return (IGoal) super.getGoal();
	}
	
}
