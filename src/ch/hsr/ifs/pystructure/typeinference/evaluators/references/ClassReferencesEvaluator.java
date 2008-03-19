/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.references;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.ClassReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.results.references.ClassReference;

//TODO: Maybe merge with FunctionReferencesEvaluator.
/**
 * Evaluator for finding all the references to a function.
 */
public class ClassReferencesEvaluator extends GoalEvaluator {

	private final Class klass;

	private List<ClassReference> references;

	public ClassReferencesEvaluator(ClassReferencesGoal goal) {
		super(goal);
		this.klass = goal.getKlass();

		this.references = goal.references;
	}

	@Override
	public List<IGoal> init() {
		return wrap(new PossibleReferencesGoal(getGoal().getContext(),
				klass.getName()));
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, GoalState state) {
		if (subgoal instanceof PossibleReferencesGoal) {
			PossibleReferencesGoal g = (PossibleReferencesGoal) subgoal;

			for (Use use : g.references) {
				if (use instanceof NameUse) {
					NameUse nameUse = (NameUse) use;

					if (nameUse.getDefinitions().contains(klass)) {
						references.add(new ClassReference(klass, use.getName().getNode()));
					}
				} else {
					// TODO: Check if it's in the right module etc.
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
