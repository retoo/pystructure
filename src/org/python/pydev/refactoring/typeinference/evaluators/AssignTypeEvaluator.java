/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.evaluators;

import java.util.List;

import org.python.pydev.refactoring.typeinference.dltk.goals.GoalState;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.goals.types.DefinitionTypeGoal;
import org.python.pydev.refactoring.typeinference.goals.types.ExpressionTypeGoal;
import org.python.pydev.refactoring.typeinference.goals.types.TupleElementTypeGoal;
import org.python.pydev.refactoring.typeinference.model.definitions.AssignDefinition;
import org.python.pydev.refactoring.typeinference.model.definitions.TupleElement;
import org.python.pydev.refactoring.typeinference.model.definitions.Value;

/**
 * Evaluator for assign nodes. For determining the type of the left hand side,
 * the type of the right hand side has to be found.
 */
public class AssignTypeEvaluator extends PythonEvaluator {

	private final AssignDefinition assignDefinition;
	
	private Object result;
	
	public AssignTypeEvaluator(DefinitionTypeGoal goal, AssignDefinition assignDefinition) {
		super(goal);
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
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		this.result = result;
		return IGoal.NO_GOALS;
	}

	@Override
	public Object produceResult() {
		return result;
	}

}
