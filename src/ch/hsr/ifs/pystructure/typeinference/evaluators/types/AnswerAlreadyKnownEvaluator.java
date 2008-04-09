/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;

public class AnswerAlreadyKnownEvaluator extends AbstractEvaluator {

	public AnswerAlreadyKnownEvaluator(AbstractTypeGoal goal, IType classType) {
		super(goal);
		
		goal.resultType.appendType(classType);
	}

	public List<IGoal> init() {
		return IGoal.NO_GOALS;
	}

	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		return IGoal.NO_GOALS;
	}
	
}
