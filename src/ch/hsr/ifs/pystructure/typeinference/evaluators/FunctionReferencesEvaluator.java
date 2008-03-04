/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.dltk.goals.GoalState;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.FunctionReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;
import ch.hsr.ifs.pystructure.typeinference.results.references.FunctionReference;

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
