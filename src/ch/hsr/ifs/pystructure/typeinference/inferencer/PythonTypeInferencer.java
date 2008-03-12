/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.inferencer;

import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.evaluators.DefaultPythonEvaluatorFactory;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;

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
