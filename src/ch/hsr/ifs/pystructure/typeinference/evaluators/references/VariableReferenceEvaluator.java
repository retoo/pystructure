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

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Name;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;

/**
 * Evaluator for the type of an unqualified name, like <code>var</code>.
 */
public class VariableReferenceEvaluator extends AbstractEvaluator {

	private final Name name;
	
	private CombinedType resultType;
	
	public VariableReferenceEvaluator(ExpressionTypeGoal goal, Name name) {
		super(goal);
		this.name = name;
		
		this.resultType = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		List<IGoal> subgoals = new ArrayList<IGoal>();
		
		ModuleContext context = getGoal().getContext();
		Module module = context.getModule();
		
		for (Use use : module.getContainedUses()) {
			if (use instanceof NameUse) {
				NameUse nameUse = (NameUse) use;
				if (name.equals(nameUse.getExpression())) {
					for (Definition definition : nameUse.getDefinitions()) {
						subgoals.add(new DefinitionTypeGoal(getGoal().getContext(), definition));
					}
				}
			}
		}
		
		return subgoals;
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		if (!(subgoal instanceof DefinitionTypeGoal)) { unexpectedSubgoal(subgoal); }
		
		DefinitionTypeGoal g = (DefinitionTypeGoal) subgoal;
		resultType.appendType(g.resultType);
		return IGoal.NO_GOALS;
	}

}
