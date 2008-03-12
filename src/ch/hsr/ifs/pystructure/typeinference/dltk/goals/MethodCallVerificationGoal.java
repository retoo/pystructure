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
 * Task of this goal is to verify given possible position as a real position,
 * where given method were called.
 *
 * As result, object of ItemReference should be returned.
 */
public class MethodCallVerificationGoal extends AbstractGoal {

	private final PossiblePosition position;
	private final MethodCallsGoal goal;

	public MethodCallVerificationGoal(IContext context, MethodCallsGoal goal,
			PossiblePosition postion) {
		super(context);
		this.goal = goal;
		this.position = postion;
	}

	public PossiblePosition getPosition() {
		return position;
	}

	public MethodCallsGoal getGoal() {
		return goal;
	}

}
