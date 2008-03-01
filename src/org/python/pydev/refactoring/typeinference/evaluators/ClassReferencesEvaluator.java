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
import org.python.pydev.refactoring.typeinference.goals.references.ClassReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.references.PossibleReferencesGoal;
import org.python.pydev.refactoring.typeinference.model.definitions.Class;
import org.python.pydev.refactoring.typeinference.model.definitions.NameUse;
import org.python.pydev.refactoring.typeinference.results.references.ClassReference;

// TODO: Maybe merge with FunctionReferencesEvaluator.
/**
 * Evaluator for finding all the references to a function.
 */
public class ClassReferencesEvaluator extends PythonEvaluator {

	private final Class klass;
	
	private List<ClassReference> references;
	
	public ClassReferencesEvaluator(ClassReferencesGoal goal) {
		super(goal);
		this.klass = goal.getKlass();
		
		this.references = new ArrayList<ClassReference>();
	}

	@Override
	public List<IGoal> init() {
		return wrap(new PossibleReferencesGoal(getGoal().getContext(),
				klass.getName()));
	}
	
	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		if (subgoal instanceof PossibleReferencesGoal) {
			List<NameUse> possibleNameUses = (List<NameUse>) result;
			for (NameUse nameUse : possibleNameUses) {
				if (nameUse.getDefinitions().contains(klass)) {
					references.add(new ClassReference(klass, nameUse.getName().getNode()));
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
