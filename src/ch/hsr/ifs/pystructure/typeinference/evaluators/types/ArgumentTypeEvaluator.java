/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.contexts.CallContext;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.CallableGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.FunctionReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.MethodReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.results.references.FunctionReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;

/**
 * Evaluator for the type of an argument.
 */
public class ArgumentTypeEvaluator extends DefinitionTypeEvaluator {

	private final Argument argument;
	
	public ArgumentTypeEvaluator(DefinitionTypeGoal goal, Argument argument) {
		super(goal, argument);
		this.argument = argument;
	}

	@Override
	public List<IGoal> init() {
		List<IGoal> subgoals = new ArrayList<IGoal>();
		Function function = argument.getFunction();
		
		ModuleContext context = getGoal().getContext();
		if (function instanceof Method 
				&& function.isFirstArgument(argument)) {
			Method method = (Method) function;
			ClassType type = new ClassType(context.getModule(), method.getKlass());
			resultType.appendType(type);
		} else {
			CallContext callContext = context.getCallContext();
			
			if (callContext != null && callContext.getFunctionReference().getDefinition().equals(function)) {
				FunctionReference reference = callContext.getFunctionReference();
				exprType expression = reference.getArgumentExpression(argument);
				subgoals.add(new ExpressionTypeGoal(callContext.getParent(), expression));
			} else {
				if (function instanceof Method) {
					subgoals.add(new MethodReferencesGoal(context, (Method) function));
				} else {
					subgoals.add(new FunctionReferencesGoal(context, function));
				}
			}
		}
		
		return subgoals;
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, GoalState state) {
		//  FIXME if (subgoal instanceof ExpressionTypeGoal && result instanceof IEvaluatedType) {
		if (subgoal instanceof ExpressionTypeGoal) {
			
			ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
			IType type =  g.resultType;
			resultType.appendType(type);
			return IGoal.NO_GOALS;
		}
		// TODO: Maybe only use one goal (CallableReferencesGoal) and do
		// dispatching based on definition.
		if (subgoal instanceof CallableGoal) {
			CallableGoal g = (CallableGoal) subgoal;
			
			List<IGoal> subgoals = new ArrayList<IGoal>();
			
			for (FunctionReference reference : g.references) {
				IGoal goal = getArgumentExpressionGoal(reference);
				if (goal != null) {
					subgoals.add(goal);
				}
			}
			
			return subgoals;
		}
		return IGoal.NO_GOALS;
	}
	
	@Override
	public boolean isCached() {
		return false;
//		return super.isCached();
	}

	private IGoal getArgumentExpressionGoal(FunctionReference reference) {
		exprType expression = reference.getArgumentExpression(argument);
		if (expression == null) {
			// This is a reference of the function without a call, e.g.:
			//   other_name_for_function = function
			return null;
		} else {
			return new ExpressionTypeGoal(getGoal().getContext(), expression);
		}
	}
	
}
