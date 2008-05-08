/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
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

import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.LoopVariableDefinition;

public class LoopVariableTypeEvaluator extends AbstractEvaluator {

	private final LoopVariableDefinition loopVariableDefinition;
	private CombinedType resultType;

	public LoopVariableTypeEvaluator(DefinitionTypeGoal goal, LoopVariableDefinition loopVariableDefinition) {
		super(goal);
		this.loopVariableDefinition = loopVariableDefinition;
		
		this.resultType = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		/*
		 * for element in list:  →  list.__iter__().next()
		 */
		
		exprType iter = loopVariableDefinition.getIter();
		Call iterCall = NodeUtils.createMethodCall(iter, "__iter__");
		Call nextCall = NodeUtils.createMethodCall(iterCall, "next");
		
		return wrap(new ExpressionTypeGoal(getGoal().getContext(), nextCall));
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		if (!(subgoal instanceof ExpressionTypeGoal)) { unexpectedSubgoal(subgoal); }
		
		ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
		resultType.appendType(g.resultType);
		return IGoal.NO_GOALS;
	}

}
