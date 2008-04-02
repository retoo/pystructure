/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
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
	public List<IGoal> subGoalDone(IGoal subgoal, GoalState state) {
		/* wie hier mit expression tuple goal umgehen */
		if (this.state == State.EXPRESSION_WAIT) {
			this.state = State.RESULT_WAIT;
			List<IGoal> subgoals = new ArrayList<IGoal>();

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
			AbstractTypeGoal g = (AbstractTypeGoal) subgoal;

			this.result.appendType(g.resultType);
			return IGoal.NO_GOALS;
		}
	}

}
