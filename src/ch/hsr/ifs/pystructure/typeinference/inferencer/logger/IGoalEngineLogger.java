package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public interface IGoalEngineLogger {

	void evaluationStarted(IGoal rootGoal);
	void evaluationFinished(IGoal rootGoal);

	void goalCreated(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator);
	void goalFinished(IGoal goal, AbstractEvaluator evaluator);

	void shutdown();

}
