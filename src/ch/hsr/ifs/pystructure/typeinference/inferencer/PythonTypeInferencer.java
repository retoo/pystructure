/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.inferencer;

import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher.PythonEvaluatorFactory;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;

public class PythonTypeInferencer implements ITypeInferencer {

	private final GoalEngine engine;
	
	public PythonTypeInferencer() {
		engine = new GoalEngine(new PythonEvaluatorFactory());
	}
	
	public PythonTypeInferencer(IGoalEngineLogger logger) {
		engine = new GoalEngine(new PythonEvaluatorFactory(), logger);
	}

	public synchronized IEvaluatedType evaluateType(AbstractTypeGoal goal, int timeLimit) {
		return (IEvaluatedType) engine.evaluateGoal(goal, new TimelimitPruner(timeLimit));
	}

	public void shutdown() {
		engine.shutdown();
	}

}
