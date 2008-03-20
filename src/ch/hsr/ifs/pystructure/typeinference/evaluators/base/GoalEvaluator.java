/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.evaluators.base;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

/**
 * Abstract goal evaluator.
 */
public abstract class GoalEvaluator {

	protected final IGoal goal;

	public GoalEvaluator(IGoal goal) {
		this.goal = goal;
	}

	public IGoal getGoal() {
		return goal;
	}

	/**
	 * Called first time to fetch primary subgoals.
	 *
	 * @return array of required subgoals or <code>IGoal.NO_GOALS</code>
	 */
	public abstract List<IGoal> init();

	/**
	 * Called when some subgoal are done.
	 *
	 * @param subgoal
	 *            completed subgoal
	 * @param state
	 *            final state of subgoal (DONE, PRUNED or RECURSION)
	 * @return array of new required subgoals or <code>IGoal.NO_GOALS</code>
	 */
	public abstract List<IGoal> subGoalDone(IGoal subgoal, GoalState state);

	/**
	 * Called when all posted subgoals are done
	 *
	 * @return result of evaluation this goal
	 */
	public final Object produceResult() {
		return null;
	}

	/**
	 * Wraps a single goal in a list and returns it, for convenience.
	 * @param goal
	 * @return a list with the goal as the only element
	 */
	protected List<IGoal> wrap(IGoal goal) {
		List<IGoal> goals = new ArrayList<IGoal>();
		goals.add(goal);
		return goals;
	}

	public boolean isCached() {
		return false;
	}

	protected void unexpectedSubGoal(IGoal goal) {
		throw new RuntimeException(this + " got an unexpected subgoal" + goal);
		
	}

}
