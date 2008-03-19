/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.references;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.FunctionReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.results.references.FunctionReference;

/**
 * Evaluator for finding all the references to a function.
 */
public class FunctionReferencesEvaluator extends GoalEvaluator {

	private final Function function;
	
	private List<FunctionReference> references;
	
	public FunctionReferencesEvaluator(FunctionReferencesGoal goal) {
		super(goal);
		this.function = goal.getFunction();
		
		this.references = goal.references;
	}

	@Override
	public List<IGoal> init() {
		return wrap(new PossibleReferencesGoal(getGoal().getContext(),
				function.getName()));
	}
	
	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, GoalState state) {
		if (subgoal instanceof PossibleReferencesGoal) {
			PossibleReferencesGoal g = (PossibleReferencesGoal) subgoal;
			
			for (Use use : g.references) {
				/*
				 * The PossibleReferencesEvaluator might return AttributeUses
				 * which we dont really need at this point
				 */
				if (use instanceof NameUse) {
					NameUse nameUse = (NameUse) use;
					if (nameUse.getDefinitions().contains(function)) {
						references.add(new FunctionReference(function, use.getName().getNode()));
					}
					// TODO: Check if it's in the right module etc.
				} else {
					/* skip */
				}
			}
		}
		return IGoal.NO_GOALS;
	}

	@Override
	public Object produceResult() {
		return references;
	}

}
