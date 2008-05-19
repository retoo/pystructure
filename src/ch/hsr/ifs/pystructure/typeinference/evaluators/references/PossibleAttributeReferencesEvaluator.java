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

import org.python.pydev.parser.jython.ast.Attribute;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleAttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AttributeUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

/**
 * Evaluator for finding uses of a name, which could be possible references to a
 * function or method.
 */
public class PossibleAttributeReferencesEvaluator extends AbstractEvaluator {

	private String name;
	private Map<IGoal, AttributeUse> useForGoal;
	
	private List<AttributeReference> references;

	public PossibleAttributeReferencesEvaluator(PossibleAttributeReferencesGoal goal) {
		super(goal);
		name = goal.getName();
		
		useForGoal = new HashMap<IGoal, AttributeUse>();
		
		references = goal.references;
	}

	@Override
	public List<IGoal> init() {
		Workspace workspace = getGoal().getContext().getWorkspace();
		List<AttributeReference> possibleAttributeReferences = workspace.getPossibleAttributeReferences(name);
		if (possibleAttributeReferences != null) {
			references.addAll(possibleAttributeReferences);
		}
		return wrap(new PossibleReferencesGoal(getGoal().getContext(), name));
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		
		if (subgoal instanceof PossibleReferencesGoal) {
			PossibleReferencesGoal g = (PossibleReferencesGoal) subgoal;
			
			List<IGoal> subgoals = new LinkedList<IGoal>();

			for (Use use : g.references) {
				
				if (use instanceof AttributeUse) {
					AttributeUse attributeUse = (AttributeUse) use;
					
					ModuleContext parentContext = getGoal().getContext();
					ModuleContext context = new ModuleContext(parentContext, attributeUse.getModule());
					
					Attribute attribute = (Attribute) attributeUse.getExpression();
					IGoal goal = new ExpressionTypeGoal(context, attribute.value);
					
					useForGoal.put(goal, attributeUse);
					subgoals.add(goal);
				}
			}
			
			return subgoals;
			
		} else if (subgoal instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
			
			AttributeUse use = useForGoal.get(g);
			Module module = g.getContext().getModule();
			AttributeReference ref = new AttributeReference(use.getName(), g.resultType, use.getExpression(), module);
			
			references.add(ref);
			
		} else {
			unexpectedSubgoal(subgoal);
		}
			
		return IGoal.NO_GOALS;
	}

}
