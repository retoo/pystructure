/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
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
import ch.hsr.ifs.pystructure.typeinference.contexts.InstanceContext;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ReturnTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
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
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		
		if (subgoal instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal expressionTypeGoal = (ExpressionTypeGoal) subgoal; 
			List<IGoal> subgoals = new ArrayList<IGoal>();
			
			for (IType type : expressionTypeGoal.resultType) {
				
				if (type instanceof FunctionType) {
					// It's a function or method call.
					
					FunctionType functionType = (FunctionType) type;
					
					ModuleContext context = getGoal().getContext();
					Module module = context.getModule();
					
					FunctionReference reference;
					if (type instanceof MethodType) {
						MethodType methodType = (MethodType) type;
						reference = new MethodReference(methodType.getMethod(), call.func, module);
						context = new InstanceContext(context, module, methodType.getClassType());
					} else {
						reference = new FunctionReference(functionType.getFunction(), call.func, module);
					}
					
					// Module of call context may be different from that of the
					// function definition, therefore the two contexts.
					CallContext callContext = new CallContext(context, module, reference);
					ModuleContext moduleContext = new ModuleContext(callContext, functionType.getModule());
					
					subgoals.add(new ReturnTypeGoal(moduleContext, functionType.getFunction()));
				} else if (type instanceof MetaclassType) {
					// It's a constructor.
					
					MetaclassType metaclassType = (MetaclassType) type;
					ClassType classType = new ClassType(metaclassType.getKlass(), call);
					resultType.appendType(classType);
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
