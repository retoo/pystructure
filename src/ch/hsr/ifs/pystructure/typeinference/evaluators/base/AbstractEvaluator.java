/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.base;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.utils.ListUtils;

public abstract class AbstractEvaluator {

	private final IGoal goal;

	public AbstractEvaluator(IGoal goal) {
		this.goal = goal;
	}

	/**
	 * Initializes the evaluator. The evaluator can, if it likes, return a list
	 * of subgoals. The list will get processed eventually and reported as
	 * finished using the method {@link #subgoalDone(IGoal, GoalState)}.
	 * 
	 * return {@link IGoal#NO_GOALS} if you have no goals
	 * 
	 * @return list of subgoals
	 */
	public abstract List<IGoal> init();

	/**
	 * Gets called when a subgoal has been finished. The evaluator can create new subgoals if it likes 
	 * 
	 * @param subgoal finished subgoal
	 * @param state state of the subgoal (might be RECURSIVE if the goal was causing a recurssion)
	 * 
	 * return {@link IGoal#NO_GOALS} if you have no goals
	 * 
	 * @return list of subgoals
	 */
	public abstract List<IGoal> subgoalDone(IGoal subgoal, GoalState state);
	
	public void finish() {
	}
	
	/**
	 * Can be implemented if a evaluator supports caching. If it does it has to
	 * check if the goal's result is already cached. If it is cached it has to load it into the goal. 
	 * 
	 * @return whether the result is already cached
	 */
	public boolean checkCache() {
		return false;
	}
	
	public IGoal getGoal() {
		return goal;
	}
	
	/* Helper methods */

	/**
	 * @see ListUtils#wrap(Object)
	 */
	protected static List<IGoal> wrap(IGoal goal) {
		return ListUtils.wrap(goal);
	}

	protected void unexpectedSubgoal(IGoal goal) {
		throw new RuntimeException(this + " got an unexpected subgoal" + goal);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
