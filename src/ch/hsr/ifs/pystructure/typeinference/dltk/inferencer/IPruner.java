/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.dltk.inferencer;

import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;

/**
 * Pruner is thing, that can prune some goals from working queue. It could be
 * time limits, goals count or more complex criterias. Pruners are created per
 * every evaluation.
 */
public interface IPruner {

	/**
	 * Are called when evaluating were started.
	 */
	void init();

	/**
	 * Called every time before getting new goal from evaluating queue.
	 * 
	 * @param goal
	 *            goal to prune
	 * @param stat
	 *            information about created evaluator
	 */
	boolean prune(IGoal goal, EvaluatorStatistics stat);
}
