package ch.hsr.ifs.pystructure.playground;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class WorkUnit {
	public enum State { NEW, INITIALIZED, DONE };
	
	public final IGoal goal;
	public final AbstractEvaluator evaluator;
	final WorkUnit parent;
	
	State state;
	
	private int subgoalsCount;
	private int subgoalsDoneCount;
	
	public WorkUnit(IGoal goal, AbstractEvaluator createEvaluator, WorkUnit parent) {
		this.state = State.NEW;
		this.goal = goal;
		this.parent = parent;
		this.evaluator = createEvaluator;
	}

	public boolean isNew() {
		return state == State.NEW;
	}

	public void setInitialized() {
		state = State.INITIALIZED;
	}

	public boolean isInitialized() {
		return state == State.INITIALIZED;
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

	List<IGoal> subGoalDone(IGoal goal) {
		List<IGoal> newSubGoals = evaluator.subGoalDone(goal, null);
		subgoalsDoneCount++;
		subgoalsCount += newSubGoals.size();
		return newSubGoals;
	}

	boolean isDone() {
		return state == State.DONE;
	}
	
}
