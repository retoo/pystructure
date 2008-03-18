package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class StatsLogger implements IGoalEngineLogger {
	private IGoal currentGoal;
	private int currentSubGoalsCounter;
	private StringBuilder out;
	private int rootGoalsCounter;
	private int subGoalsCounter;
	
	public StatsLogger() {
		this.out = new StringBuilder();
		this.rootGoalsCounter = 0;
		this.subGoalsCounter = 0;
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

	public void goalCreated(IGoal goal, GoalEvaluator creator, GoalEvaluator evaluator) {
		currentSubGoalsCounter++;
		subGoalsCounter++;
	}

	public void goalFinished(IGoal goal, GoalEvaluator evaluator) {
	}

	public void shutdown() {
		System.out.println("");
		System.out.println("Statistics");
		System.out.println("");
		System.out.println(out);
		System.out.println("Total root goals: " + rootGoalsCounter );
		System.out.println("Total SubGoals: " + subGoalsCounter );
		System.out.println("Average Subgoals / Goal: " + subGoalsCounter / rootGoalsCounter);
	}
	
}
