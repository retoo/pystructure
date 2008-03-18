package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class CombinedLogger implements IGoalEngineLogger {
	private final IGoalEngineLogger[] loggers;

	public CombinedLogger(IGoalEngineLogger... loggers) {
		this.loggers = loggers;
	}
	

	public void evaluationFinished(IGoal rootGoal) {
		for (IGoalEngineLogger logger : loggers) {
			logger.evaluationFinished(rootGoal);
		}
	}

	public void evaluationStarted(IGoal rootGoal) {
		for (IGoalEngineLogger logger : loggers) {
			logger.evaluationStarted(rootGoal);
		}
	}

	public void goalCreated(IGoal goal, GoalEvaluator creator, GoalEvaluator evaluator) {
		for (IGoalEngineLogger logger : loggers) {
			logger.goalCreated(goal, creator, evaluator);
		}
	}

	public void goalFinished(IGoal goal, GoalEvaluator evaluator) {
		for (IGoalEngineLogger logger : loggers) {
			logger.goalFinished(goal, evaluator);
		}
	}

	public void shutdown() {
		for (IGoalEngineLogger logger : loggers) {
			logger.shutdown();
		}
	}

}
