/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Return;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.visitors.scope.ReturnVisitor;

import ch.hsr.ifs.pystructure.typeinference.dltk.goals.GoalState;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.types.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ReturnTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.results.types.CombinedType;

/**
 * Evaluator for the return type of a function or method.
 */
public class ReturnTypeEvaluator extends PythonEvaluator {

	private final Function function;
	
	private CombinedType resultType;
	
	public ReturnTypeEvaluator(ReturnTypeGoal goal) {
		super(goal);
		function = goal.getFunction();
		
		resultType = new CombinedType(); 
	}

	@Override
	public List<IGoal> init() {
		List<IGoal> subgoals = new ArrayList<IGoal>();
		
		List<Return> returns = ReturnVisitor.findReturns(function.getNode());
		for (Return r : returns) {
			exprType value = r.value;
			subgoals.add(new ExpressionTypeGoal(getGoal().getContext(), value));
		}
		
		return subgoals;
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		if (result instanceof IEvaluatedType) {
			IEvaluatedType type = (IEvaluatedType) result;
			resultType.appendType(type);
		}
		return IGoal.NO_GOALS;
	}

	@Override
	public Object produceResult() {
		return resultType;
	}

}
