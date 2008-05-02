package ch.hsr.ifs.pystructure.playground.fibonacci;

import ch.hsr.ifs.pystructure.typeinference.inferencer.GoalEngine;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.CombinedLogger;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.StatsLogger;

public class Fibonacci {
	
	private GoalEngine goalEngine;
	
	public Fibonacci() {
		CombinedLogger logger = new CombinedLogger(new StatsLogger(false));
		
		FibonacciEvaluatorFactory factory = new FibonacciEvaluatorFactory();
		goalEngine = new GoalEngine(factory, logger);
	}

	public int calc(int i) {
		FibonacciGoal fiboGoal = new FibonacciGoal(i);
		goalEngine.evaluateGoal(fiboGoal);
		
		return fiboGoal.result;
	}
	
	public void shutdown() {
		goalEngine.shutdown();
	}
	
	public static void main(String[] args) {
		Fibonacci fib = new Fibonacci();
		
		for (int i = 0; i < 20; i++) {
			System.out.println("" + i +  ": " + fib.calc(i));
		}
		
		fib.shutdown();
	}

}
