/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Tuple;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.dltk.goals.GoalState;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.types.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.TupleElementTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.TupleElement;
import ch.hsr.ifs.pystructure.typeinference.results.types.TupleType;

public class TupleElementTypeEvaluator extends PythonEvaluator {

	private enum State { EXPRESSION_WAIT, RESULT_WAIT };
	private State state;
	private final TupleElement element;
	
	private Object result;

	public TupleElementTypeEvaluator(TupleElementTypeGoal goal) {
		super(goal);
		element = goal.getTupleElement();
	}

	@Override
	public List<IGoal> init() {
		exprType expression = element.getExpression();
		
		state = State.EXPRESSION_WAIT;
		return wrap(new ExpressionTypeGoal(getGoal().getContext(), expression));
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		
		if (this.state == State.EXPRESSION_WAIT) {
			this.state = State.RESULT_WAIT;
			List<IGoal> subgoals = new ArrayList<IGoal>();
			
			for (IEvaluatedType type : EvaluatorUtils.extractTypes((IEvaluatedType) result)) {
				if (type instanceof TupleType) {
					Tuple tuple = ((TupleType) type).getTuple();
					exprType childExpression = tuple.elts[element.getFirstIndex()];

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
			this.result = result;
			return IGoal.NO_GOALS;
		}
	}

	@Override
	public Object produceResult() {
		return result;
	}

}
