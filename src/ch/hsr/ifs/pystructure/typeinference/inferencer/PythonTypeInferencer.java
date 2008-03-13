/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.inferencer;

import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher.DefaultPythonEvaluatorFactory;

public class PythonTypeInferencer implements ITypeInferencer {

	private final GoalEngine engine;
	
	public PythonTypeInferencer() {
		engine = new GoalEngine(new DefaultPythonEvaluatorFactory());
	}

	public synchronized IEvaluatedType evaluateType(AbstractTypeGoal goal, int timeLimit) {
		return (IEvaluatedType) engine.evaluateGoal(goal, new TimelimitPruner(timeLimit));
	}

	public synchronized Object evaluateGoal(IGoal goal, IPruner pruner) {
		return engine.evaluateGoal(goal, pruner);
	}

}
