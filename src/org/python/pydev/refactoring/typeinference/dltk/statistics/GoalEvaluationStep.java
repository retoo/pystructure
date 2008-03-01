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

import java.util.List;

public class GoalEvaluationStep {
	public static final int INIT = 0;
	public static final int DEFAULT = 1;
	public static final int RESULT = 2;

	private final int kind;
	private long time;
	private List<GoalEvaluationStatistics> subgoalsStats;
	private Object result;

	public GoalEvaluationStep(int kind) {
		super();
		this.kind = kind;
	}

	public GoalEvaluationStep(int kind, long time,
			List<GoalEvaluationStatistics> subgoalsStats, Object result) {
		super();
		this.kind = kind;
		this.time = time;
		this.subgoalsStats = subgoalsStats;
		this.result = result;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<GoalEvaluationStatistics> getSubgoalsStats() {
		return subgoalsStats;
	}

	public void setSubgoalsStats(List<GoalEvaluationStatistics> subgoalsStats) {
		this.subgoalsStats = subgoalsStats;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getKind() {
		return kind;
	}

}
