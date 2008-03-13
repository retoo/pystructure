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

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class FixedAnswerEvaluator extends GoalEvaluator {

	private final Object result;

	public FixedAnswerEvaluator(IGoal goal, Object result) {
		super(goal);
		this.result = result;
	}

	public List<IGoal> init() {
		return IGoal.NO_GOALS;
	}

	public Object produceResult() {
		return result;
	}

	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		return IGoal.NO_GOALS;
	}

}
