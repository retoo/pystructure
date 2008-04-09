package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class StatsLogger implements IGoalEngineLogger {

	private IGoal currentGoal;
	private int currentSubGoalsCounter;
	private StringBuilder out;
	private int rootGoalsCounter;
	private int subGoalsCounter;
	private long start;
	private boolean showRootGoalStats;
	
	public StatsLogger(boolean showRootGoalStatsl) {
		this.showRootGoalStats = showRootGoalStatsl;
		this.out = new StringBuilder();
		this.rootGoalsCounter = 0;
		this.subGoalsCounter = 0;
		
		this.start = System.currentTimeMillis();
	}
	
	public StatsLogger() {
		this(true);
	}

	public void evaluationStarted(IGoal rootGoal) {
		assert currentGoal == null;
		this.currentGoal = rootGoal;
		this.currentSubGoalsCounter = 0;
		this.rootGoalsCounter++;
	}

	public void evaluationFinished(IGoal rootGoal) {
		assert rootGoal == currentGoal;
		out.append("" + currentGoal + "." + currentSubGoalsCounter + "\n");
		this.currentGoal = null;
		this.currentSubGoalsCounter = 0;

	}

	public void goalCreated(IGoal goal, AbstractEvaluator creator, AbstractEvaluator evaluator) {
		currentSubGoalsCounter++;
		subGoalsCounter++;
	}

	public void goalFinished(IGoal goal, AbstractEvaluator evaluator) {
	}

	public void shutdown() {
		long delta = System.currentTimeMillis() - start;
		
		System.out.println("");
		System.out.println("Statistics");
		System.out.println("");
		
		if (showRootGoalStats) {
			System.out.println(out);
		}
		
		System.out.println("Total root goals: " + rootGoalsCounter);
		System.out.println("Total subgoals: " + subGoalsCounter);
		if (rootGoalsCounter != 0) {
			System.out.println("Average subgoals / goal: " + subGoalsCounter / rootGoalsCounter);
		}
		System.out.println();
		System.out.println("Time: " + delta);
		if (subGoalsCounter != 0) {
			System.out.println("Average time / subgoal: " + delta / subGoalsCounter);
		}
		if  (rootGoalsCounter != 0) {
			System.out.println("Average time / root goal: " + delta / rootGoalsCounter);
		}
		System.out.println("Average subgoal / s: " + subGoalsCounter * 1000L / delta);
		System.out.println("Average rootgoal / s: " + rootGoalsCounter * 1000L / delta);
		System.out.println("(Note: Benchmarking might be broken on Windows due to some problems with its timer.)");
		
	}
	
}
