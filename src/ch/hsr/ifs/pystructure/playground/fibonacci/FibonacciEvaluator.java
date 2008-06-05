package ch.hsr.ifs.pystructure.playground.fibonacci;

import java.util.LinkedList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class FibonacciEvaluator extends AbstractEvaluator {

	private int index;
	private FibonacciGoal goal;

	public FibonacciEvaluator(FibonacciGoal goal) {
		super(goal);
		
		this.goal = goal;
		this.index = goal.getIndex();
	}
	
	@Override
	public List<IGoal> init() {
		List<IGoal> subgoals = new LinkedList<IGoal>();
		
		if (index <= 1) {
			goal.result = index;
		} else {
			subgoals.add(new FibonacciGoal(index - 1));
			subgoals.add(new FibonacciGoal(index - 2));
		}
		
		return subgoals;
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		if (subgoal instanceof FibonacciGoal) {
			FibonacciGoal g = (FibonacciGoal) subgoal;
			
			goal.result += g.result;
		} else {
			this.unexpectedSubgoal(subgoal);
		}
		
		return IGoal.NO_GOALS;
	}
	
}
