/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.dltk.goals;

import ch.hsr.ifs.pystructure.typeinference.dltk.contexts.IContext;

/**
 * Task of this goal is to verify given possible position as a real position,
 * where given field were read or changed.
 *
 * As result, object of ItemReference or null should be returned.
 */
public class FieldPositionVerificationGoal extends AbstractGoal {

	private final PossiblePosition position;
	private final FieldReferencesGoal goal;

	public FieldPositionVerificationGoal(IContext context,
			FieldReferencesGoal goal, PossiblePosition postion) {
		super(context);
		this.goal = goal;
		this.position = postion;
	}

	public PossiblePosition getPosition() {
		return position;
	}

	public FieldReferencesGoal getGoal() {
		return goal;
	}

}
