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
import ch.hsr.ifs.pystructure.typeinference.goals.references.FunctionReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.results.references.FunctionReference;

/**
 * Evaluator for finding all the references to a function.
 */
public class FunctionReferencesEvaluator extends AbstractEvaluator {

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
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		if (!(subgoal instanceof PossibleReferencesGoal)) { unexpectedSubgoal(subgoal); }
		
		PossibleReferencesGoal g = (PossibleReferencesGoal) subgoal;

		for (Use use : g.references) {
			/*
			 * The PossibleReferencesEvaluator might return AttributeUses
			 * which we dont really need at this point
			 */
			if (use instanceof NameUse) {
				NameUse nameUse = (NameUse) use;
				if (nameUse.getDefinitions().contains(function)) {
					references.add(new FunctionReference(function, use.getExpression(), nameUse.getModule()));
				}
				// TODO: Check if it's in the right module etc.
			} else {
				/* skip */
			}
		}

		return IGoal.NO_GOALS;
	}

}
