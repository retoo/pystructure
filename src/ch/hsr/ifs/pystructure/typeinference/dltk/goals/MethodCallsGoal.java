/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.dltk.goals;

import ch.hsr.ifs.pystructure.typeinference.contexts.IContext;

/**
 * Task of this goal is to find all calls of the given method. Evaluator for
 * this goal is registered by default and uses standard DLTK search. Cause
 * without full type information it is impossible to get fully correct results,
 * goal will send MethodCallVerificationGoals. So, to get them working user
 * should register appropriate evaluator.
 *
 * Result of this goal is array of ItemReference objects
 */
public class MethodCallsGoal extends AbstractReferencesGoal {

	public MethodCallsGoal(IContext context, String methodName,
			String parentModelKey) {
		super(context, methodName, parentModelKey);
	}

}
