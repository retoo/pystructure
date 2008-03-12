/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.inferencer;

import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.inferencer.DefaultTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.dltk.inferencer.IPruner;
import ch.hsr.ifs.pystructure.typeinference.dltk.inferencer.TimelimitPruner;
import ch.hsr.ifs.pystructure.typeinference.evaluators.PythonEvaluatorFactory;

public class PythonTypeInferencer extends DefaultTypeInferencer {

	public PythonTypeInferencer() {
		super(new PythonEvaluatorFactory());
	}

	public synchronized IEvaluatedType evaluateType(AbstractTypeGoal goal, int timeLimit) {
		return super.evaluateType(goal, new TimelimitPruner(timeLimit));
	}

	public synchronized Object evaluateGoal(IGoal goal, IPruner pruner) {
		return super.evaluateGoal(goal, pruner);
	}

}
