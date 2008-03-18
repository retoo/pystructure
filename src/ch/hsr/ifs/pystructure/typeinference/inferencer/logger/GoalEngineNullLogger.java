package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class GoalEngineNullLogger implements IGoalEngineLogger {

	public void evaluationFinished(IGoal rootGoal) {
	}

	public void evaluationStarted(IGoal rootGoal) {
	}

	public void goalCreated(IGoal goal, GoalEvaluator creator, GoalEvaluator evaluator) {
	}

	public void goalFinished(IGoal goal, GoalEvaluator evaluator) {
	}

	public void shutdown() {
	}

}
