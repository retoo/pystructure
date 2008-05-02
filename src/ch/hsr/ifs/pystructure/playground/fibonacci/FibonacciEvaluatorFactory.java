package ch.hsr.ifs.pystructure.playground.fibonacci;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher.IEvaluatorFactory;

public class FibonacciEvaluatorFactory implements IEvaluatorFactory {

	public AbstractEvaluator createEvaluator(IGoal goal) {
		if (goal instanceof FibonacciGoal) {
			return new FibonacciEvaluator((FibonacciGoal) goal);
		}
		
		throw new RuntimeException("Unknown goal " + goal);
	}

}
