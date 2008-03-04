/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
/**
 *
 */
package ch.hsr.ifs.pystructure.typeinference.dltk.inferencer;

import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;

public class TimelimitPruner implements IPruner {

	private long timeStart;
	private final long timeLimit;

	public TimelimitPruner(long timeLimit) {
		super();
		this.timeLimit = timeLimit;
	}

	public void init() {
		this.timeStart = System.currentTimeMillis();
	}

	public boolean prune(IGoal goal, EvaluatorStatistics stat) {
		if (timeLimit > 0 && System.currentTimeMillis() - timeStart > timeLimit) {
			return true;
		}
		return false;
	}
}
