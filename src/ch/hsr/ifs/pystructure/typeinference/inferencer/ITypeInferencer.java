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

/**
 *
 *
 */
public interface ITypeInferencer {

	/**
	 * Should evaluate type for a "type goal". Type goal is an abstract thing
	 * with a context, that represents some kind of evaluation (expression type,
	 * method return type, etc.) So, inferencer should know about kinds of tasks
	 * it should do.
	 *
	 * @param goal
	 * @param timeLimit
	 *            time in milliseconds, or -1 if no limits.
	 * @return
	 */
	IEvaluatedType evaluateType(AbstractTypeGoal goal, int timeLimit);
}
