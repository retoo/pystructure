/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;

public class FixedAnswerEvaluator extends AbstractEvaluator {

	public FixedAnswerEvaluator(DefinitionTypeGoal goal, IType result) {
		super(goal);
		
		goal.resultType.appendType(result);
	}

	public FixedAnswerEvaluator(ExpressionTypeGoal goal, IType classType) {
		super(goal);
		
		goal.resultType.appendType(classType);
	}

	public List<IGoal> init() {
		return IGoal.NO_GOALS;
	}

	public List<IGoal> subGoalDone(IGoal subgoal, GoalState state) {
		return IGoal.NO_GOALS;
	}

}
