package ch.hsr.ifs.pystructure.typeinference.inferencer;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public interface IGoalEngineLogger {

	void goalCreated(IGoal goal, GoalEvaluator creator, GoalEvaluator evaluator);

	void goalFinished(IGoal goal);

}
