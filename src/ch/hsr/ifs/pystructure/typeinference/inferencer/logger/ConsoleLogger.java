/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import java.io.PrintStream;
import java.util.IdentityHashMap;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.utils.StringUtils;

public class ConsoleLogger implements IGoalEngineLogger {
	private static final PrintStream OUT = System.out;
	
	private IdentityHashMap<AbstractEvaluator, AbstractEvaluator> creators;
	private IdentityHashMap<Object, Integer> numbers;
	private int curNr = 1;
	
	public ConsoleLogger() {
		this.creators = new IdentityHashMap<AbstractEvaluator, AbstractEvaluator>(); 
		this.numbers = new IdentityHashMap<Object, Integer>();
	}
	
	public void evaluationFinished(IGoal rootGoal) {
		OUT.println("Evaluation finished of " + rootGoal);
	}

	public void evaluationStarted(IGoal rootGoal) {
		OUT.println("Evaluation started of " + rootGoal);
		
	}

	public void goalCreated(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator) {
		numbers.put(evaluator, curNr++);
		creators.put(evaluator, creator);
		say(evaluator, "Created " + evaluator.getClass().getSimpleName() + " "  + goal);
	}
	
	public void goalFinished(IGoal goal, AbstractEvaluator evaluator) {
		say(evaluator, "Finished " + goal);
	}

	private void say(AbstractEvaluator evaluator, String text) {
		AbstractEvaluator creator = getCreater(evaluator);
		OUT.println(StringUtils.multiply(level(evaluator), "|   ")
				+ getNumber(creator)
				+ " " + getNumber(evaluator)
				+ " " + text);
	}

	private int getNumber(AbstractEvaluator evaluator) {
		Integer nr = numbers.get(evaluator);
		return nr != null ? (int) nr : 0; 
	}

	private int level(AbstractEvaluator evaluator) {
		int level = 0;
		
		for (AbstractEvaluator creator = evaluator; creator != null; creator = getCreater(creator)) {
			level++;
		} 
		
		return level;
	}

	private AbstractEvaluator getCreater(AbstractEvaluator creator) {
		return creators.get(creator);
	}

	public void shutdown() {
		OUT.println("Engine finished");
	}

}
