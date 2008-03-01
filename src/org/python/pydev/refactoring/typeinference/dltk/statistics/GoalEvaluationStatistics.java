/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

/**
 *
 */
package org.python.pydev.refactoring.typeinference.dltk.statistics;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.refactoring.typeinference.dltk.evaluators.GoalEvaluator;
import org.python.pydev.refactoring.typeinference.dltk.goals.GoalState;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;

public class GoalEvaluationStatistics {
	private IGoal goal;
	private GoalEvaluator evaluator;
	private GoalState state;
	private long timeStart;
	private long timeEnd;
	private List steps;
	private GoalEvaluationStatistics parentStat;

	public GoalEvaluationStatistics(IGoal goal) {
		super();
		this.timeStart = System.currentTimeMillis();
		this.timeEnd = -1;
		this.goal = goal;
		this.state = GoalState.WAITING;
		this.steps = new ArrayList();
	}

	public GoalEvaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(GoalEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	public GoalState getState() {
		return state;
	}

	public void setState(GoalState state) {
		this.state = state;
	}

	public long getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(long timeEnd) {
		this.timeEnd = timeEnd;
	}

	public GoalEvaluationStatistics getParentStat() {
		return parentStat;
	}

	public void setParentStat(GoalEvaluationStatistics parentStat) {
		this.parentStat = parentStat;
	}

	public IGoal getGoal() {
		return goal;
	}

	public long getTimeStart() {
		return timeStart;
	}

	public List getSteps() {
		return steps;
	}

}
