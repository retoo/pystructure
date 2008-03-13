/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.dispatcher;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.IGoalEvaluatorFactory;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

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
