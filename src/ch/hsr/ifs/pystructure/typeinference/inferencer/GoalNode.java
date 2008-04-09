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

package ch.hsr.ifs.pystructure.typeinference.inferencer;

import java.util.LinkedList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class GoalNode {

	private enum State { NEW, INITIALIZED, FINISHED };
	private State state;
	
	public final IGoal goal;
	public final AbstractEvaluator evaluator;
	public final List<GoalNode> parents;
	
	private int subgoalsCount;
	private int subgoalsDoneCount;
	
	public GoalNode(IGoal goal, AbstractEvaluator createEvaluator, GoalNode parent) {
		this.state = State.NEW;
		this.goal = goal;
		this.evaluator = createEvaluator;
		this.parents = new LinkedList<GoalNode>();
		if (parent != null) {
			addParent(parent);
		}
	}
	
	public void addParent(GoalNode parent) {
		parents.add(parent);
	}

	public boolean isNew() {
		return state == State.NEW;
	}

	public boolean isInitialized() {
		return state == State.INITIALIZED;
	}

	public boolean isFinished() {
		return state == State.FINISHED;
	}
	
	public List<IGoal> init() {
		List<IGoal> subgoals = evaluator.init();
		state = State.INITIALIZED;
		subgoalsCount = subgoals.size();
		return subgoals;
	}
	
	public void finish() {
		evaluator.finish();
		state = State.FINISHED;
	}

	public boolean areAllSubgoalsDone() {
		return subgoalsDoneCount == subgoalsCount;
	}

	public List<IGoal> subGoalDone(IGoal goal, GoalState state) {
		List<IGoal> newSubGoals = evaluator.subgoalDone(goal, state);
		subgoalsDoneCount++;
		subgoalsCount += newSubGoals.size();
		return newSubGoals;
	}
	
	public List<IGoal> subgoalDone(IGoal goal) {
		return subGoalDone(goal, GoalState.DONE);
	}
	
	/**
	 * Checks if this GoalNode appears somewhere in the parents (at any level)
	 * of the other GoalNode.
	 * 
	 * @param other GoalNode to start checking from
	 * @return true if this is in the parents of other
	 */
	public boolean isInParentsOf(GoalNode other) {
		if (this == other) {
			return true;
		}
		for (GoalNode parent : other.parents) {
			if (isInParentsOf(parent)) {
				return true;
			}
		}
		return false;
	}

}
