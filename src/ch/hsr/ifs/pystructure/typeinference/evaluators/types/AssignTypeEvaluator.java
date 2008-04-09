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

package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.TupleElementTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AssignDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.TupleElement;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Value;

/**
 * Evaluator for assign nodes. For determining the type of the left hand side,
 * the type of the right hand side has to be found.
 */
public class AssignTypeEvaluator extends DefinitionTypeEvaluator  {

	private final AssignDefinition assignDefinition;
	
	public AssignTypeEvaluator(DefinitionTypeGoal goal, AssignDefinition assignDefinition) {
		super(goal, assignDefinition);
		this.assignDefinition = assignDefinition;
	}

	@Override
	public List<IGoal> init() {
		Value value = assignDefinition.getValue();
		if (value instanceof TupleElement) {
			return wrap(new TupleElementTypeGoal(getGoal().getContext(), (TupleElement) value));
		} else {
			return wrap(new ExpressionTypeGoal(getGoal().getContext(), assignDefinition.getValue().getExpression()));
		}
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		AbstractTypeGoal g = (AbstractTypeGoal) subgoal;
		resultType.appendType(g.resultType);
		return IGoal.NO_GOALS;
	}

}
