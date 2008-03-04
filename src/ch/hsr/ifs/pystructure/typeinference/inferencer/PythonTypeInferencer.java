/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.inferencer;

import ch.hsr.ifs.pystructure.typeinference.dltk.goals.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.inferencer.DefaultTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.dltk.inferencer.EvaluatorStatistics;
import ch.hsr.ifs.pystructure.typeinference.dltk.inferencer.IPruner;
import ch.hsr.ifs.pystructure.typeinference.dltk.types.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.PythonEvaluatorFactory;

public class PythonTypeInferencer extends DefaultTypeInferencer {

	private static class SimplestPruner implements IPruner {

		private long timeStart;
		private final long timeLimit;

		public SimplestPruner(long timeLimit) {
			super();
			this.timeLimit = timeLimit;
		}

		public void init() {
			this.timeStart = System.currentTimeMillis();
		}

		public boolean prune(IGoal goal, EvaluatorStatistics stat) {
			long currentTime = System.currentTimeMillis();
			if (timeLimit != -1 && currentTime - timeStart > timeLimit) {
				return true;
			}
			// Commented because it is kind of mean.
			//if (stat != null && stat.getSubGoalsDoneSuccessful() > 5) {
			//	return true;
			//}
			return false;
		}

	}

	public PythonTypeInferencer() {
		super(new PythonEvaluatorFactory());
	}

	public synchronized IEvaluatedType evaluateType(AbstractTypeGoal goal, int timeLimit) {
		return super.evaluateType(goal, new SimplestPruner(timeLimit));
	}

	public synchronized Object evaluateGoal(IGoal goal, IPruner pruner) {
		return super.evaluateGoal(goal, pruner);
	}

}
