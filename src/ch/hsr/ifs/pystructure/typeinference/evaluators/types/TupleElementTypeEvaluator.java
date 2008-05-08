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

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Tuple;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.TupleElementTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.TupleElement;
import ch.hsr.ifs.pystructure.typeinference.results.types.TupleType;

public class TupleElementTypeEvaluator extends AbstractEvaluator {

	private enum State { EXPRESSION_WAIT, RESULT_WAIT };
	private State state;
	private final TupleElement element;
	private CombinedType result;

	public TupleElementTypeEvaluator(TupleElementTypeGoal goal) {
		super(goal);
		element = goal.getTupleElement();
		
		this.result = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		exprType expression = element.getExpression();

		state = State.EXPRESSION_WAIT;
		return wrap(new ExpressionTypeGoal(getGoal().getContext(), expression));
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		/* wie hier mit expression tuple goal umgehen */
		if (this.state == State.EXPRESSION_WAIT) {
			this.state = State.RESULT_WAIT;
			List<IGoal> subgoals = new ArrayList<IGoal>();

			if (!(subgoal instanceof ExpressionTypeGoal)) { unexpectedSubgoal(subgoal); }
			
			ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;

			for (IType type : g.resultType) {
				if (type instanceof TupleType) {
					Tuple tuple = ((TupleType) type).getTuple();
					
					int index = element.getFirstIndex();
					if (index >= tuple.elts.length) {
						// More tuple elements are unpacked than are available
						// in the right hand side tuple. This only happens in
						// complex situations and then we ignore this tuple
						// element. Example:
						// 
						// a, b, c = 1, 2
						continue;
					}
					exprType childExpression = tuple.elts[index];

					if (element.getIndexesCount() == 1) {
						subgoals.add(new ExpressionTypeGoal(getGoal().getContext(), childExpression));
					} else {
						TupleElement childElement = new TupleElement(childExpression, element.getRestOfIndexes());
						subgoals.add(new TupleElementTypeGoal(getGoal().getContext(), childElement));
					}
				} else {
					// Maybe it's an array, we can't yet handle this.
				}
			}

			return subgoals;

		} else {
			if (!(subgoal instanceof AbstractTypeGoal)) { unexpectedSubgoal(subgoal); }
			
			AbstractTypeGoal g = (AbstractTypeGoal) subgoal;

			this.result.appendType(g.resultType);
			return IGoal.NO_GOALS;
		}
	}

}
