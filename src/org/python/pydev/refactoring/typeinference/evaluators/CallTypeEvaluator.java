/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.refactoring.typeinference.contexts.CallContext;
import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.dltk.goals.GoalState;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;
import org.python.pydev.refactoring.typeinference.goals.types.ExpressionTypeGoal;
import org.python.pydev.refactoring.typeinference.goals.types.PythonTypeGoal;
import org.python.pydev.refactoring.typeinference.goals.types.ReturnTypeGoal;
import org.python.pydev.refactoring.typeinference.results.references.FunctionReference;
import org.python.pydev.refactoring.typeinference.results.references.MethodReference;
import org.python.pydev.refactoring.typeinference.results.types.ClassType;
import org.python.pydev.refactoring.typeinference.results.types.CombinedType;
import org.python.pydev.refactoring.typeinference.results.types.FunctionType;
import org.python.pydev.refactoring.typeinference.results.types.MetaclassType;
import org.python.pydev.refactoring.typeinference.results.types.MethodType;

/**
 * Evaluator for Call nodes. It could be a constructor, a method call or a
 * normal function call.
 */
public class CallTypeEvaluator extends PythonEvaluator {

	private final Call call;
	
	private CombinedType resultType;
	
	public CallTypeEvaluator(PythonTypeGoal goal, Call call) {
		super(goal);
		this.call = call;
		
		this.resultType = new CombinedType();
	}

	@Override
	public List<IGoal> init() {
		exprType func = call.func;
		return wrap(new ExpressionTypeGoal(getGoal().getContext(), func));
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		if (subgoal instanceof ExpressionTypeGoal) {
			List<IGoal> subgoals = new ArrayList<IGoal>();
			
			for (IEvaluatedType type : EvaluatorUtils.extractTypes((IEvaluatedType) result)) {
				
				if (type instanceof FunctionType) {
					// It's function or method call.
					
					FunctionType functionType = (FunctionType) type;
					
					FunctionReference reference;
					if (type instanceof MethodType) {
						reference = new MethodReference(((MethodType) type).getMethod(), call.func);
					} else {
						reference = new FunctionReference(functionType.getFunction(), call.func);
					}
					
					PythonContext context = getGoal().getContext();
					// Module of call context may be different from that of the
					// function definition, therefore the two contexts.
					CallContext callContext = new CallContext(context, context.getModule(), reference);
					PythonContext moduleContext = new PythonContext(callContext, functionType.getModule());
					
					subgoals.add(new ReturnTypeGoal(moduleContext, functionType.getFunction()));
				}
				
				if (type instanceof MetaclassType) {
					// It's a constructor.
					MetaclassType metaclassType = (MetaclassType) type;
					resultType.appendType(new ClassType(metaclassType.getModule(), metaclassType.getKlass()));
				}
			}
			
			return subgoals;
		}
		
		if (subgoal instanceof ReturnTypeGoal && result instanceof IEvaluatedType) {
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
