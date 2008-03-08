/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.contexts.CallContext;
import ch.hsr.ifs.pystructure.typeinference.contexts.PythonContext;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.GoalState;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.types.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.goals.references.FunctionReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.MethodReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.PythonTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.results.references.FunctionReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.CombinedType;

/**
 * Evaluator for the type of an argument.
 */
public class ArgumentTypeEvaluator extends PythonEvaluator {

	private final Argument argument;
	
	private CombinedType resultType;
	
	public ArgumentTypeEvaluator(PythonTypeGoal goal, Argument argument) {
		super(goal);
		this.argument = argument;
		
		this.resultType = new CombinedType();
	}

	@Override
	public List<IGoal> init() {
		List<IGoal> subgoals = new ArrayList<IGoal>();
		
		Function function = argument.getFunction();
		
		PythonContext context = getGoal().getContext();
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
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		if (subgoal instanceof ExpressionTypeGoal && result instanceof IEvaluatedType) {
			IEvaluatedType type = (IEvaluatedType) result;
			resultType.appendType(type);
			return IGoal.NO_GOALS;
		}
		// TODO: Maybe only use one goal (CallableReferencesGoal) and do
		// dispatching based on definition.
		if (subgoal instanceof FunctionReferencesGoal 
		 || subgoal instanceof MethodReferencesGoal) {
			List<FunctionReference> references = (List<FunctionReference>) result;
			
			List<IGoal> subgoals = new ArrayList<IGoal>();
			
			for (FunctionReference reference : references) {
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
	public Object produceResult() {
		return resultType;
	}
	
	private IGoal getArgumentExpressionGoal(FunctionReference reference) {
		exprType expression = reference.getArgumentExpression(argument);
		return new ExpressionTypeGoal(getGoal().getContext(), expression);
	}
	
}