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

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.IfExp;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;

public class IfExpTypeEvaluator extends AbstractEvaluator {
	
	private final IfExp ifExp;
	private CombinedType resultType;

	public IfExpTypeEvaluator(ExpressionTypeGoal goal, IfExp ifExp) {
		super(goal);
		this.ifExp = ifExp;
		
		this.resultType = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		List<IGoal> subgoals = new ArrayList<IGoal>();
		subgoals.add(new ExpressionTypeGoal(getGoal().getContext(), ifExp.body));
		subgoals.add(new ExpressionTypeGoal(getGoal().getContext(), ifExp.orelse));
		return subgoals;
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		if (!(subgoal instanceof ExpressionTypeGoal)) { unexpectedSubgoal(subgoal); }
		
		ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
		resultType.appendType(g.resultType);
		return IGoal.NO_GOALS;
	}

}
