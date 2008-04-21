package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import java.util.IdentityHashMap;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public abstract class GoalTreeLogger implements IGoalEngineLogger {

	protected IdentityHashMap<AbstractEvaluator, AbstractEvaluator> creators;
	protected IdentityHashMap<Object, Integer> numbers;
	private int curNr = 1;

	public GoalTreeLogger() {
		this.creators = new IdentityHashMap<AbstractEvaluator, AbstractEvaluator>(); 
		this.numbers = new IdentityHashMap<Object, Integer>();
	}
	
	public void evaluationStarted(IGoal rootGoal) {
	}
	
	public void evaluationFinished(IGoal rootGoal) {
	}
	
	public void shutdown() {
	}

	public void goalCreated(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator) {
		numbers.put(evaluator, curNr++);
		creators.put(evaluator, creator);
	}

	public void goalFinished(IGoal goal, AbstractEvaluator evaluator) {
		AbstractEvaluator creator = getCreater(evaluator);
		goalFinished(goal, creator, evaluator);
	}
	
	protected void goalFinished(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator) {
	}
	
	protected int getNumber(AbstractEvaluator evaluator) {
		Integer nr = numbers.get(evaluator);
		return nr != null ? (int) nr : 0; 
	}

	protected int level(AbstractEvaluator evaluator) {
		int level = 0;
		
		for (AbstractEvaluator creator = evaluator; creator != null; creator = getCreater(creator)) {
			level++;
		} 
		
		return level;
	}

	private AbstractEvaluator getCreater(AbstractEvaluator creator) {
		return creators.get(creator);
	}

}
