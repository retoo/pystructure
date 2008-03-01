/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.dltk.goals.GoalState;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;
import org.python.pydev.refactoring.typeinference.goals.types.DefinitionTypeGoal;
import org.python.pydev.refactoring.typeinference.goals.types.ExpressionTypeGoal;
import org.python.pydev.refactoring.typeinference.model.definitions.Definition;
import org.python.pydev.refactoring.typeinference.model.definitions.Module;
import org.python.pydev.refactoring.typeinference.model.definitions.NameUse;
import org.python.pydev.refactoring.typeinference.model.definitions.Use;
import org.python.pydev.refactoring.typeinference.results.types.CombinedType;

/**
 * Evaluator for the type of an unqualified name, like <code>var</code>.
 */
public class VariableReferenceEvaluator extends PythonEvaluator {

	private final Name name;
	
	private CombinedType resultType;
	
	public VariableReferenceEvaluator(ExpressionTypeGoal goal, Name name) {
		super(goal);
		this.name = name;
		
		this.resultType = new CombinedType();
	}

	@Override
	public List<IGoal> init() {
		List<IGoal> subgoals = new ArrayList<IGoal>();
		
		PythonContext context = getGoal().getContext();
		Module module = context.getModule();
		
		for (Use use : module.getContainedUses()) {
			if (use instanceof NameUse) {
				NameUse nameUse = (NameUse) use;
				if (name.equals(nameUse.getName().getNode())) {
					for (Definition definition : nameUse.getDefinitions()) {
						subgoals.add(new DefinitionTypeGoal(getGoal().getContext(), definition));
					}
				}
			}
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
