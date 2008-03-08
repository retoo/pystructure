/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Name;

import ch.hsr.ifs.pystructure.typeinference.contexts.PythonContext;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.GoalState;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.types.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.results.types.CombinedType;

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