/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.dltk.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.python.pydev.refactoring.typeinference.dltk.evaluators.GoalEvaluator;
import org.python.pydev.refactoring.typeinference.dltk.goals.GoalState;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;

/**
 * Records all evaluation tree including evaluation times
 *
 */
public class GoalEvalutationRecorder implements IEvaluationStatisticsRequestor {

	private IGoal rootRoal;
	private Map goalStats = new HashMap();

	public GoalEvalutationRecorder duplicate() {
		GoalEvalutationRecorder n = new GoalEvalutationRecorder();
		n.rootRoal = rootRoal;
		for (Iterator iterator = goalStats.keySet().iterator(); iterator
				.hasNext();) {
			Object k = iterator.next();
			n.goalStats.put(k, goalStats.get(k));
		}
		return n;
	}

	public void evaluationStarted(IGoal rootGoal) {
		reset();
		this.rootRoal = rootGoal;
		this.goalStats.put(rootGoal, new GoalEvaluationStatistics(rootGoal));
	}

	private void reset() {
		this.rootRoal = null;
		this.goalStats = new HashMap();
	}

	private GoalEvaluationStatistics addGoalStatistics(
			GoalEvaluationStatistics parent, IGoal g) {
		GoalEvaluationStatistics s = new GoalEvaluationStatistics(g);
		s.setParentStat(parent);
		goalStats.put(g, s);
		return s;
	}

	private List<GoalEvaluationStatistics> createEmptyGoalStatistics(
			GoalEvaluationStatistics parent, List<IGoal> subgoals) {
		List<GoalEvaluationStatistics> r = new ArrayList<GoalEvaluationStatistics>();
		for (IGoal subgoal : subgoals) {
			r.add(addGoalStatistics(parent, subgoal));
		}
		return r;
	}

	public void evaluatorInitialized(GoalEvaluator evaluator, List<IGoal> subgoals,
			long time) {
		appendStep(evaluator, subgoals, null, time, GoalEvaluationStep.INIT);
	}

	public void evaluatorProducedResult(GoalEvaluator evaluator, Object result,
			long time) {
		GoalEvaluationStatistics s = appendStep(evaluator, null, result, time,
				GoalEvaluationStep.RESULT);
		if (s != null) {
			s.setTimeEnd(System.currentTimeMillis());
		}
	}

	public void evaluatorReceivedResult(GoalEvaluator evaluator,
			IGoal finishedGoal, List<IGoal> subgoals, long time) {
		appendStep(evaluator, subgoals, null, time, GoalEvaluationStep.DEFAULT);
	}

	private GoalEvaluationStatistics appendStep(GoalEvaluator evaluator,
			List<IGoal> subgoals, Object result, long time, int kind) {
		IGoal goal = evaluator.getGoal();
		GoalEvaluationStatistics stat = (GoalEvaluationStatistics) this.goalStats
				.get(goal);
		if (stat != null) {
			GoalEvaluationStep step = new GoalEvaluationStep(kind);
			step.setTime(time);
			if (subgoals != null) {
				step.setSubgoalsStats(
						createEmptyGoalStatistics(stat, subgoals));
			}
			step.setResult(result);
			stat.getSteps().add(step);
			return stat;
		} else {
			System.err.println("Unknown goal: " + goal);
		}
		return null;
	}

	public void goalEvaluatorAssigned(IGoal goal, GoalEvaluator evaluator) {
		GoalEvaluationStatistics stat = (GoalEvaluationStatistics) this.goalStats
				.get(goal);
		if (stat != null) {
			stat.setEvaluator(evaluator);
		} else {
			System.err.println("Unknown goal: " + goal);
		}
	}

	public void goalStateChanged(IGoal goal, GoalState state, GoalState oldState) {
		GoalEvaluationStatistics stat = (GoalEvaluationStatistics) this.goalStats
				.get(goal);
		if (stat != null) {
			stat.setState(state);
		} else {
			System.err.println("Unknown goal: " + goal);
		}
	}

	public IGoal getRootRoal() {
		return rootRoal;
	}

	public GoalEvaluationStatistics getStatisticsForGoal(IGoal g) {
		return (GoalEvaluationStatistics) this.goalStats.get(g);
	}

}
