package ch.hsr.ifs.pystructure.typeinference.evaluators.base;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.utils.ListUtils;

public abstract class AbstractEvaluatorNew {

	private final IGoal goal;

	public AbstractEvaluatorNew(IGoal goal) {
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

}
