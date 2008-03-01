/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.python.pydev.refactoring.typeinference.dltk.evaluators;

import org.python.pydev.refactoring.typeinference.dltk.goals.FieldPositionVerificationGoal;
import org.python.pydev.refactoring.typeinference.dltk.goals.FieldReferencesGoal;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.dltk.goals.PossiblePosition;

//import org.eclipse.dltk.core.search.IDLTKSearchConstants;
//import org.eclipse.dltk.core.search.SearchPattern;

public class FieldReferencesGoalEvaluator extends SearchBasedGoalEvaluator {

	public FieldReferencesGoalEvaluator(IGoal goal) {
		super(goal);
	}

//	protected SearchPattern createSearchPattern() {
//		FieldReferencesGoal goal = (FieldReferencesGoal) getGoal();
//		String name = goal.getName();
//		return SearchPattern.createPattern(name, IDLTKSearchConstants.FIELD,
//				IDLTKSearchConstants.REFERENCES, SearchPattern.R_EXACT_MATCH);
//	}

	protected IGoal createVerificationGoal(PossiblePosition pos) {
		return new FieldPositionVerificationGoal(this.getGoal().getContext(),
				(FieldReferencesGoal) this.getGoal(), pos);
	}

}
