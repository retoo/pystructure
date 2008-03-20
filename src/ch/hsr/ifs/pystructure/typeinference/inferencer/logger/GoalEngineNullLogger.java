package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class GoalEngineNullLogger implements IGoalEngineLogger {

	public void evaluationFinished(IGoal rootGoal) {
	}

	public void evaluationStarted(IGoal rootGoal) {
	}

	public void goalCreated(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator) {
	}

	public void goalFinished(IGoal goal, AbstractEvaluator evaluator) {
	}

	public void shutdown() {
	}

}
