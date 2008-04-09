/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.inferencer.logger;

import java.io.PrintStream;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class StatsLogger implements IGoalEngineLogger {

	private static final PrintStream OUT = System.err;

	private IGoal currentGoal;
	private int currentSubGoalsCounter;
	private StringBuilder rootGoalStats;
	private int rootGoalsCounter;
	private int subGoalsCounter;
	private long start;
	private boolean showRootGoalStats;
	
	public StatsLogger(boolean showRootGoalStatsl) {
		this.showRootGoalStats = showRootGoalStatsl;
		this.rootGoalStats = new StringBuilder();
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
		rootGoalStats.append("" + currentGoal + "." + currentSubGoalsCounter + "\n");
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
		
		OUT.println("");
		OUT.println("Statistics");
		OUT.println("");
		
		if (showRootGoalStats) {
			OUT.println(rootGoalStats);
		}
		
		OUT.println("Total root goals: " + rootGoalsCounter);
		OUT.println("Total subgoals: " + subGoalsCounter);
		if (rootGoalsCounter != 0) {
			OUT.println("Average subgoals / goal: " + subGoalsCounter / rootGoalsCounter);
		}
		OUT.println();
		OUT.println("Time: " + delta / 1000.0 + "s");
		if (subGoalsCounter != 0) {
			OUT.println("Average time / subgoal: " + delta / subGoalsCounter + "ms");
		}
		if  (rootGoalsCounter != 0) {
			OUT.println("Average time / root goal: " + delta / rootGoalsCounter + "ms");
		}
		OUT.println("Average subgoal / s: " + subGoalsCounter * 1000 / delta);
		OUT.println("Average rootgoal / s: " + rootGoalsCounter * 1000L / delta);
	}
	
}
