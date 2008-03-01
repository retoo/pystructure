/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.refactoring.typeinference.dltk.goals.GoalState;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.goals.references.FunctionReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.references.PossibleReferencesGoal;
import org.python.pydev.refactoring.typeinference.model.definitions.Function;
import org.python.pydev.refactoring.typeinference.model.definitions.NameUse;
import org.python.pydev.refactoring.typeinference.results.references.FunctionReference;

/**
 * Evaluator for finding all the references to a function.
 */
public class FunctionReferencesEvaluator extends PythonEvaluator {

	private final Function function;
	
	private List<FunctionReference> references;
	
	public FunctionReferencesEvaluator(FunctionReferencesGoal goal) {
		super(goal);
		this.function = goal.getFunction();
		
		this.references = new ArrayList<FunctionReference>();
	}

	@Override
	public List<IGoal> init() {
		return wrap(new PossibleReferencesGoal(getGoal().getContext(),
				function.getName()));
	}
	
	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		if (subgoal instanceof PossibleReferencesGoal) {
			List<NameUse> possibleNameUses = (List<NameUse>) result;
			for (NameUse nameUse : possibleNameUses) {
				if (nameUse.getDefinitions().contains(function)) {
					references.add(new FunctionReference(function, nameUse.getName().getNode()));
				}
				// TODO: Check if it's in the right module etc.
			}
		}
		return IGoal.NO_GOALS;
	}

	@Override
	public Object produceResult() {
		return references;
	}

}
