/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.references;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
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
public class ClassReferencesEvaluator extends AbstractEvaluator {

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
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		if (subgoal instanceof PossibleReferencesGoal) {
			PossibleReferencesGoal g = (PossibleReferencesGoal) subgoal;

			for (Use use : g.references) {
				if (use instanceof NameUse) {
					NameUse nameUse = (NameUse) use;

					if (nameUse.getDefinitions().contains(klass)) {
						references.add(new ClassReference(klass, use.getNode()));
					}
				} else {
					// TODO: Check if it's in the right module etc.
				}
			}
			
		} else {
			unexpectedSubgoal(subgoal);
		}
		
		return IGoal.NO_GOALS;
	}

}
