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

package ch.hsr.ifs.pystructure.typeinference.evaluators.references;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.FunctionReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.results.references.FunctionReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.FunctionType;

/**
 * Evaluator for finding all the references to a function.
 */
public class FunctionReferencesEvaluator extends AbstractEvaluator {

	private final Function function;
	
	private final Map<IGoal, Use> useForGoal;
	
	private List<FunctionReference> references;
	
	public FunctionReferencesEvaluator(FunctionReferencesGoal goal) {
		super(goal);
		this.function = goal.getFunction();
		
		this.useForGoal = new HashMap<IGoal, Use>();
		
		this.references = goal.references;
	}

	@Override
	public List<IGoal> init() {
		return wrap(new PossibleReferencesGoal(getGoal().getContext(),
				function.getName()));
	}
	
	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		if (subgoal instanceof PossibleReferencesGoal) {
			PossibleReferencesGoal g = (PossibleReferencesGoal) subgoal;

			List<IGoal> subgoals = new LinkedList<IGoal>();
			
			for (Use use : g.references) {
				ModuleContext context = new ModuleContext(getGoal().getContext(), use.getModule());
				ExpressionTypeGoal exprTypeGoal = new ExpressionTypeGoal(context, use.getExpression());
				useForGoal.put(exprTypeGoal, use);
				subgoals.add(exprTypeGoal);
			}
			
			return subgoals;

		} else if (subgoal instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
			Use use = useForGoal.get(g);
			
			for (IType type : g.resultType) {
				if (type instanceof FunctionType) {
					Function resultFunction = ((FunctionType) type).getFunction();
					if (function.equals(resultFunction)) {
						FunctionReference r = new FunctionReference(function, use.getExpression(), use.getModule());
						references.add(r);
					}
				}
			}
			
			return IGoal.NO_GOALS;
			
		} else {
			unexpectedSubgoal(subgoal);
		}
		
		return IGoal.NO_GOALS;
	}

}
