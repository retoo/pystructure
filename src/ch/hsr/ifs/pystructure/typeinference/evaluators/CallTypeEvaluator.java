/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.contexts.CallContext;
import ch.hsr.ifs.pystructure.typeinference.contexts.PythonContext;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.GoalState;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ReturnTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.results.references.FunctionReference;
import ch.hsr.ifs.pystructure.typeinference.results.references.MethodReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.FunctionType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MethodType;

/**
 * Evaluator for Call nodes. It could be a constructor, a method call or a
 * normal function call.
 */
public class CallTypeEvaluator extends PythonEvaluator {

	private final Call call;
	
	private CombinedType resultType;
	
	public CallTypeEvaluator(AbstractTypeGoal goal, Call call) {
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
