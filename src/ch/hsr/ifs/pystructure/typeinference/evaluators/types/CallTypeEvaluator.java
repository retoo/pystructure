/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.contexts.CallContext;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
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
public class CallTypeEvaluator extends AbstractEvaluator {

	private final Call call;
	
	private CombinedType resultType;
	
	public CallTypeEvaluator(ExpressionTypeGoal goal, Call call) {
		super(goal);
		this.call = call;
		
		this.resultType = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		exprType func = call.func;
		return wrap(new ExpressionTypeGoal(getGoal().getContext(), func));
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		
		if (subgoal instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal expressionTypeGoal = (ExpressionTypeGoal) subgoal; 
			List<IGoal> subgoals = new ArrayList<IGoal>();
			
			for (IType type : expressionTypeGoal.resultType) {
				
				if (type instanceof FunctionType) {
					// It's function or method call.
					
					FunctionType functionType = (FunctionType) type;
					
					FunctionReference reference;
					if (type instanceof MethodType) {
						reference = new MethodReference(((MethodType) type).getMethod(), call.func);
					} else {
						reference = new FunctionReference(functionType.getFunction(), call.func);
					}
					
					ModuleContext context = getGoal().getContext();
					// Module of call context may be different from that of the
					// function definition, therefore the two contexts.
					CallContext callContext = new CallContext(context, context.getModule(), reference);
					ModuleContext moduleContext = new ModuleContext(callContext, functionType.getModule());
					
					subgoals.add(new ReturnTypeGoal(moduleContext, functionType.getFunction()));
				}
				
				if (type instanceof MetaclassType) {
					// It's a constructor.
					MetaclassType metaclassType = (MetaclassType) type;
					resultType.appendType(new ClassType(metaclassType.getModule(), metaclassType.getKlass()));
				}
			}
			
			return subgoals;
			
		} else if (subgoal instanceof ReturnTypeGoal) {
			ReturnTypeGoal g = (ReturnTypeGoal) subgoal;
			resultType.appendType(g.resultType);
			
		} else {
			unexpectedSubgoal(subgoal);
		}
		
		return IGoal.NO_GOALS;
	}
	
}
