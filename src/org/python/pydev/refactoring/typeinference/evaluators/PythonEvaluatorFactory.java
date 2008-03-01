/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.evaluators;

import org.python.pydev.refactoring.typeinference.dltk.evaluators.GoalEvaluator;
import org.python.pydev.refactoring.typeinference.dltk.evaluators.IGoalEvaluatorFactory;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;

/**
 * Evaluator factory which, given a goal, creates the appropriate evaluator. It
 * can be seen as a kind of dispatcher.
 */
public class PythonEvaluatorFactory implements IGoalEvaluatorFactory {

	private static final IGoalEvaluatorFactory FACTORY = new DefaultPythonEvaluatorFactory();

	public GoalEvaluator createEvaluator(IGoal goal) {
		return FACTORY.createEvaluator(goal);
	}

}
