package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import java.io.PrintStream;
import java.util.IdentityHashMap;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.utils.StringUtils;

public class ConsoleLogger implements IGoalEngineLogger {
	private final static PrintStream out = System.out;
	
	private IdentityHashMap<GoalEvaluator, GoalEvaluator> creators;
	private IdentityHashMap<Object, Integer> numbers;
	private int curNr = 0;
	
	public ConsoleLogger() {
		this.creators = new IdentityHashMap<GoalEvaluator, GoalEvaluator>(); 
		this.numbers = new IdentityHashMap<Object, Integer>();
	}
	
	public void evaluationFinished(IGoal rootGoal) {
		out.println("Evaluation finished of " + rootGoal);
	}

	public void evaluationStarted(IGoal rootGoal) {
		out.println("Evaluation started of " + rootGoal);
		
	}

	public void goalCreated(IGoal goal, GoalEvaluator creator, GoalEvaluator evaluator) {
		numbers.put(evaluator, curNr++);
		creators.put(evaluator, creator);
		say(evaluator, "Created " + evaluator.getClass().getSimpleName() + " "  + goal);
	}
	
	public void goalFinished(IGoal goal, GoalEvaluator evaluator) {
		say(evaluator, "Finished " + goal);
	}

	private void say(GoalEvaluator evaluator, String text) {
		GoalEvaluator creator = getCreater(evaluator);
		out.println(StringUtils.multiply(level(evaluator), "|   ")
				+ getNumber(creator)
				+ " " + getNumber(evaluator)
				+ " " + text);
	}

	private int getNumber(GoalEvaluator evaluator) {
		Integer nr = numbers.get(evaluator);
		return nr != null ? (int) nr : -1; 
	}

	private int level(GoalEvaluator evaluator) {
		int level = 0;
		
		for (GoalEvaluator creator = evaluator; creator != null; creator = getCreater(creator)) {
			level++;
		} 
		
		return level;
	}

	private GoalEvaluator getCreater(GoalEvaluator creator) {
		return creators.get(creator);
	}


}
