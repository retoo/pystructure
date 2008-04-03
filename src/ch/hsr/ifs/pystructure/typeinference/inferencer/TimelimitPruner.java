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
package ch.hsr.ifs.pystructure.typeinference.inferencer;

import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;

public class TimelimitPruner implements IPruner {

	private long timeStart;
	private final long timeLimit;

	public TimelimitPruner(long timeLimit) {
		this.timeLimit = timeLimit;
	}

	public void init() {
		timeStart = System.currentTimeMillis();
	}

	public boolean prune(IGoal goal) {
		long timeElapsed = System.currentTimeMillis() - timeStart;
		return (timeLimit > 0 && timeElapsed > timeLimit);
	}

}
