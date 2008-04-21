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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.ClassReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.results.references.ClassReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;

//TODO: Maybe merge with FunctionReferencesEvaluator.
/**
 * Evaluator for finding all the references to a function.
 */
public class ClassReferencesEvaluator extends AbstractEvaluator {

	private final Class klass;

	private List<ClassReference> references;

	private Map<IGoal, List<Use>> usesForGoal;

	public ClassReferencesEvaluator(ClassReferencesGoal goal) {
		super(goal);
		this.klass = goal.getKlass();

		this.references = goal.references;
		
		this.usesForGoal = new HashMap<IGoal, List<Use>>();
	}

	@Override
	public List<IGoal> init() {
		return wrap(new PossibleReferencesGoal(getGoal().getContext(),
				klass.getName()));
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		if (subgoal instanceof PossibleReferencesGoal) {
			PossibleReferencesGoal g = (PossibleReferencesGoal) subgoal;

			for (Use use : g.references) {
				ModuleContext context = new ModuleContext(getGoal().getContext(), use.getModule());
				
				if (use instanceof NameUse) {
					NameUse nameUse = (NameUse) use;

					for (Definition definition : nameUse.getDefinitions()) {
						DefinitionTypeGoal goal = new DefinitionTypeGoal(context, definition);
						
						List<Use> uses = usesForGoal.get(goal);
						if (uses == null) {
							uses = new LinkedList<Use>();
							usesForGoal.put(goal, uses);
						}
						uses.add(use);
					}
					
				} else {
					// TODO: Add processing of AttributeUse and merge with FunctionReferencesEvaluator
					// TODO: What about imports like these?: from module import Class as C
				}
			}
			
			List<IGoal> subgoals = new ArrayList<IGoal>();
			for (IGoal goal : usesForGoal.keySet()) {
				subgoals.add(goal);
			}
			
			return subgoals;
			
		} else if (subgoal instanceof DefinitionTypeGoal) {
			DefinitionTypeGoal g = (DefinitionTypeGoal) subgoal;
			
			for (IType type : g.resultType) {
				if (type instanceof MetaclassType) {
					MetaclassType metaclassType = (MetaclassType) type;
					if (metaclassType.getKlass().equals(klass)) {
						for (Use use : usesForGoal.get(subgoal)) {
							references.add(new ClassReference(klass, use.getExpression()));
						}
						break;
					}
				}
			}
			
		} else {
			unexpectedSubgoal(subgoal);
		}
		
		return IGoal.NO_GOALS;
	}

}
