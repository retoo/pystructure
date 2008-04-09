package ch.hsr.ifs.pystructure.playground;

import java.util.LinkedList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class WorkUnit {

	public enum State { NEW, INITIALIZED, FINISHED };
	
	public final IGoal goal;
	public final AbstractEvaluator evaluator;
	public final List<WorkUnit> parents;
	
	public State state;
	
	private int subgoalsCount;
	private int subgoalsDoneCount;
	
	public WorkUnit(IGoal goal, AbstractEvaluator createEvaluator, WorkUnit parent) {
		this.state = State.NEW;
		this.goal = goal;
		this.evaluator = createEvaluator;
		this.parents = new LinkedList<WorkUnit>();
		if (parent != null) {
			addParent(parent);
		}
	}
	
	public void addParent(WorkUnit parent) {
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

	public boolean areAllSubgoalsDone() {
		return subgoalsDoneCount == subgoalsCount;
	}

	public List<IGoal> subGoalDone(IGoal goal, GoalState state) {
		List<IGoal> newSubGoals = evaluator.subgoalDone(goal, state);
		subgoalsDoneCount++;
		subgoalsCount += newSubGoals.size();
		return newSubGoals;
	}
	
	public List<IGoal> subGoalDone(IGoal goal) {
		return subGoalDone(goal, GoalState.DONE);
	}
	
	/**
	 * Checks if this WorkUnit appears somewhere in the parents (at any level)
	 * of the other WorkUnit.
	 * 
	 * @param other WorkUnit to start checking from
	 * @return true if this is in the parents of other
	 */
	public boolean isInParentsOf(WorkUnit other) {
		if (this == other) {
			return true;
		}
		for (WorkUnit parent : other.parents) {
			if (isInParentsOf(parent)) {
				return true;
			}
		}
		return false;
	}

}
