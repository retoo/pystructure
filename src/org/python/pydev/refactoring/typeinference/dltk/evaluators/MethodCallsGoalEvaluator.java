/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.python.pydev.refactoring.typeinference.dltk.evaluators;

import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.dltk.goals.MethodCallVerificationGoal;
import org.python.pydev.refactoring.typeinference.dltk.goals.MethodCallsGoal;
import org.python.pydev.refactoring.typeinference.dltk.goals.PossiblePosition;

//import org.eclipse.dltk.core.search.IDLTKSearchConstants;
//import org.eclipse.dltk.core.search.SearchPattern;

public class MethodCallsGoalEvaluator extends SearchBasedGoalEvaluator {

	public MethodCallsGoalEvaluator(IGoal goal) {
		super(goal);
	}

//	protected SearchPattern createSearchPattern() {
//		MethodCallsGoal goal = (MethodCallsGoal) getGoal();
//		String name = goal.getName();
//		return SearchPattern.createPattern(name, IDLTKSearchConstants.METHOD,
//				IDLTKSearchConstants.REFERENCES, SearchPattern.R_EXACT_MATCH);
//	}

	protected IGoal createVerificationGoal(PossiblePosition pos) {
		MethodCallVerificationGoal g = new MethodCallVerificationGoal(this
				.getGoal().getContext(), (MethodCallsGoal) this.getGoal(), pos);
		return g;
	}

}
