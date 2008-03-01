/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.evaluators;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.dltk.goals.GoalState;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;
import org.python.pydev.refactoring.typeinference.goals.references.PossibleAttributeReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.references.PossibleReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.types.DefinitionTypeGoal;
import org.python.pydev.refactoring.typeinference.goals.types.ExpressionTypeGoal;
import org.python.pydev.refactoring.typeinference.goals.types.PythonTypeGoal;
import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;
import org.python.pydev.refactoring.typeinference.model.definitions.AttributeUse;
import org.python.pydev.refactoring.typeinference.model.definitions.Definition;
import org.python.pydev.refactoring.typeinference.model.definitions.IAttributeDefinition;
import org.python.pydev.refactoring.typeinference.model.definitions.Module;
import org.python.pydev.refactoring.typeinference.model.definitions.NameUse;
import org.python.pydev.refactoring.typeinference.model.definitions.Use;
import org.python.pydev.refactoring.typeinference.results.references.AttributeReference;

/**
 * Evaluator for finding uses of a name, which could be possible references to a
 * function or method.
 */
public class PossibleAttributeReferencesEvaluator extends PythonEvaluator {

	private NameAdapter name;
	private Map<IGoal, NameAdapter> attributeNames;
	private Map<IGoal, SimpleNode> attributeNodes;
	
	private List<AttributeReference> references;

	public PossibleAttributeReferencesEvaluator(PossibleAttributeReferencesGoal goal) {
		super(goal);
		name = goal.getName();
		// IdentityHashMap is used so that we have separate entries for equal
		// goals, because it may happen when we have a IAttributeDefinition and
		// we'll create the same goal twice but for different nodes.
		attributeNames = new IdentityHashMap<IGoal, NameAdapter>();
		attributeNodes = new IdentityHashMap<IGoal, SimpleNode>();
		
		references = new ArrayList<AttributeReference>();
	}

	@Override
	public List<IGoal> init() {
		return wrap(new PossibleReferencesGoal(getGoal().getContext(), name));
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		if (subgoal instanceof PossibleReferencesGoal) {
			List<IGoal> subgoals = new LinkedList<IGoal>();
			List<Use> uses = (List<Use>) result;
			
			for (Use use : uses) {
				SimpleNode node = use.getName().getNode();
				PythonContext parentContext = getGoal().getContext();
				PythonContext context = new PythonContext(parentContext, use.getModule());
				
				if (use instanceof AttributeUse) {
					AttributeUse attributeUse = (AttributeUse) use;
					Attribute attribute = (Attribute) attributeUse.getNode();
					IGoal goal = new ExpressionTypeGoal(context, attribute.value);
					
					attributeNames.put(goal, attributeUse.getName());
					attributeNodes.put(goal, attribute);
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
							attributeNodes.put(goal, node);
							subgoals.add(goal);
						}
					}
				}
			}
			
			return subgoals;
			
		} else {
			PythonTypeGoal typeGoal = (PythonTypeGoal) subgoal;
			IEvaluatedType type = (IEvaluatedType) result;
			NameAdapter name = attributeNames.get(subgoal);
			SimpleNode node = attributeNodes.get(subgoal);
			Module module = typeGoal.getContext().getModule();
			references.add(new AttributeReference(name, type, node, module));
		}
			
		return IGoal.NO_GOALS;
	}

	@Override
	public Object produceResult() {
		return references;
	}

}
