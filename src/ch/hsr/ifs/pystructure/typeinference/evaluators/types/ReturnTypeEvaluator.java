/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Return;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.visitors.scope.ReturnVisitor;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ReturnTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;

/**
 * Evaluator for the return type of a function or method.
 */
public class ReturnTypeEvaluator extends AbstractEvaluator {

	private final Function function;

	private CombinedType resultType;

	public ReturnTypeEvaluator(ReturnTypeGoal goal) {
		super(goal);
		function = goal.getFunction();

		resultType = goal.resultType; 
	}

	@Override
	public List<IGoal> init() {
		List<IGoal> subgoals = new ArrayList<IGoal>();

		List<Return> returns = ReturnVisitor.findReturns(function.getNode());
		for (Return r : returns) {
			exprType value = r.value;
			/* there might be nothing being returned */
			if (value != null) {
				subgoals.add(new ExpressionTypeGoal(getGoal().getContext(), value));
			}
		}
		
		/* well if there are no returns or if there are just empty returns we return None */
		if (subgoals.isEmpty()) {
			resultType.appendType(new ClassType("None"));
		}

		return subgoals;
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
		resultType.appendType(g.resultType);
		return IGoal.NO_GOALS;
	}

}
