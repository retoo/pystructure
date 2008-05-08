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
		List<IGoal> subGoals = new LinkedList<IGoal>();
		
		if (index == 0) {
			goal.result = 0;
		} else if (index == 1) {
			goal.result = 1;
		} else {
			subGoals.add(new FibonacciGoal(index - 1));
			subGoals.add(new FibonacciGoal(index - 2));
		}
		
		return subGoals;
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
