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
	
	public StatsLogger() {
		this.out = new StringBuilder();
		this.rootGoalsCounter = 0;
		this.subGoalsCounter = 0;
		
		this.start = System.currentTimeMillis();
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
		System.out.println(out);
		
		System.out.println("Total root goals: " + rootGoalsCounter);
		System.out.println("Total SubGoals: " + subGoalsCounter);
		System.out.println("Average Subgoals / Goal: " + subGoalsCounter / rootGoalsCounter);
		System.out.println();
		System.out.println("Time: " + delta);
		System.out.println("Avg Time/SubGoal: " + delta / subGoalsCounter);
		System.out.println("Avg Time/root goal: " + delta / rootGoalsCounter);
		System.out.println("Avg subgoal/s: " + subGoalsCounter * 1000 / delta);
		System.out.println("Avg rootgoal/s: " + rootGoalsCounter * 1000 / delta);
		System.out.println("(Note: bencharmking might be broken on windows due to some problems with its timer");
		
	}
	
}
