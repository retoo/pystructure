/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.python.pydev.refactoring.typeinference.dltk.inferencer;

/**
 * @deprecated use IEvaluationStatisticsRequestor
 */
public class EvaluatorStatistics {

	private int totalSubGoalsRequested;
	private long timeRunning;
	private int subGoalsDone;
	private int subGoalsDoneSuccessful;

	public EvaluatorStatistics(int totalSubGoalsRequested, long timeRunning,
			int subGoalsDone, int subGoalsDoneSuccessful) {
		super();
		this.totalSubGoalsRequested = totalSubGoalsRequested;
		this.timeRunning = timeRunning;
		this.subGoalsDone = subGoalsDone;
		this.subGoalsDoneSuccessful = subGoalsDoneSuccessful;
	}

	public int getTotalSubGoalsRequested() {
		return totalSubGoalsRequested;
	}

	public long getTimeRunning() {
		return timeRunning;
	}

	public int getSubGoalsDone() {
		return subGoalsDone;
	}

	public int getSubGoalsDoneSuccessful() {
		return subGoalsDoneSuccessful;
	}
}
