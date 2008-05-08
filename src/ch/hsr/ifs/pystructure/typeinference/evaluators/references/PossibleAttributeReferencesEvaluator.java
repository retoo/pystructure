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
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleAttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AttributeUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.IAttributeDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;

/**
 * Evaluator for finding uses of a name, which could be possible references to a
 * function or method.
 */
public class PossibleAttributeReferencesEvaluator extends AbstractEvaluator {

	private String name;
	private Map<IGoal, String> attributeNames;
	private Map<IGoal, exprType> attributeExpressions;
	
	private List<AttributeReference> references;

	public PossibleAttributeReferencesEvaluator(PossibleAttributeReferencesGoal goal) {
		super(goal);
		name = goal.getName();
		
		attributeNames = new HashMap<IGoal, String>();
		attributeExpressions = new HashMap<IGoal, exprType>();
		
		references = goal.references;
	}

	@Override
	public List<IGoal> init() {
		return wrap(new PossibleReferencesGoal(getGoal().getContext(), name));
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		
		if (subgoal instanceof PossibleReferencesGoal) {
			PossibleReferencesGoal g = (PossibleReferencesGoal) subgoal;
			
			List<IGoal> subgoals = new LinkedList<IGoal>();

			for (Use use : g.references) {
				exprType expression = use.getExpression();
				ModuleContext parentContext = getGoal().getContext();
				ModuleContext context = new ModuleContext(parentContext, use.getModule());
				
				if (use instanceof AttributeUse) {
					AttributeUse attributeUse = (AttributeUse) use;
					Attribute attribute = (Attribute) attributeUse.getExpression();
					IGoal goal = new ExpressionTypeGoal(context, attribute.value);
					
					attributeNames.put(goal, attributeUse.getName());
					attributeExpressions.put(goal, attribute);
					subgoals.add(goal);
					
				} else if (use instanceof NameUse) {
					// Maybe this case should better be done in
					// DefinitionVisitor, so we can only work with AttributeUse
					// here.
					
					NameUse nameUse = (NameUse) use;
					
					for (Definition definition : nameUse.getDefinitions()) {
						// TODO: Global variables should implement IAttributeDefinition
						if (definition instanceof IAttributeDefinition) {
							Definition parent = ((IAttributeDefinition) definition).getAttributeParent();
							IGoal goal = new DefinitionTypeGoal(context, parent);
							
							attributeNames.put(goal, nameUse.getName());
							attributeExpressions.put(goal, expression);
							subgoals.add(goal);
						}
					}
				}
			}
			
			return subgoals;
			
		} else if (subgoal instanceof AbstractTypeGoal) {
			AbstractTypeGoal typeGoal = (AbstractTypeGoal) subgoal;
			String name = attributeNames.get(subgoal);
			exprType expression = attributeExpressions.get(subgoal);
			Module module = typeGoal.getContext().getModule();
			AttributeReference ref = new AttributeReference(name, typeGoal.resultType, expression, module);
			references.add(ref);
			
		} else {
			unexpectedSubgoal(subgoal);
		}
			
		return IGoal.NO_GOALS;
	}

}
