/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.dltk.statistics;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.dltk.evaluators.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.GoalState;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;

public interface IEvaluationStatisticsRequestor {

	/**
	 * Called only once, when root goal were posted
	 *
	 * @param rootGoal
	 */
	void evaluationStarted(IGoal rootGoal);

	/**
	 * Called, when goal state are changed (for ex. pruned)
	 *
	 * @param goal
	 * @param state
	 */
	void goalStateChanged(IGoal goal, GoalState state, GoalState oldState);

	/**
	 * Called if goal were not pruned or considered recursive, and so evalutor
	 * were assigned to it
	 *
	 * @param goal
	 * @param evaluator
	 */
	void goalEvaluatorAssigned(IGoal goal, GoalEvaluator evaluator);

	/**
	 * Called after init() call for some goal evaluator
	 *
	 * @param evaluator
	 * @param subgoals
	 *            subgoals, that this evalutor posted
	 * @param time
	 *            time, that evaluator spent in init() method
	 */
	void evaluatorInitialized(GoalEvaluator evaluator, List<IGoal> subgoals,
			long time);

	/**
	 * Called, when evaluator accepted subgoal result, i.e. subGoalDone called
	 *
	 * @param evaluator
	 * @param subgoals
	 * @param time
	 */
	void evaluatorReceivedResult(GoalEvaluator evaluator, IGoal finishedGoal,
			List<IGoal> newGoals, long time);

	/**
	 * Called, when evaluator finally produced a result
	 *
	 * @param evaluator
	 * @param result
	 * @param time
	 */
	void evaluatorProducedResult(GoalEvaluator evaluator, Object result,
			long time);

}
