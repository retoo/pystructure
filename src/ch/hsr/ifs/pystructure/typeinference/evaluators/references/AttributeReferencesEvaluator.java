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

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.AttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleAttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Attribute;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;

/**
 * Evaluator for finding all the references to a data attribute of a class.
 */
public class AttributeReferencesEvaluator extends AbstractEvaluator {

	private final String attributeName;
	private final ClassType classType;
	
	private List<AttributeReference> references;
	private Attribute attribute;
	
	public AttributeReferencesEvaluator(AttributeReferencesGoal goal) {
		super(goal);
		this.attribute = goal.getAttribute();
		this.classType = goal.getClassType();
		this.attributeName = attribute.getName();
		
		this.references = goal.references;
	}

	@Override
	public List<IGoal> init() {
		return wrap(new PossibleAttributeReferencesGoal(getGoal().getContext(), attributeName));
	}
	
	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		PossibleAttributeReferencesGoal g = (PossibleAttributeReferencesGoal) subgoal;
		
		List<AttributeReference> possibleReferences = g.references;
		
		for (AttributeReference reference : possibleReferences) {
			for (IType type : reference.getParent()) {
				if (type.equals(classType)) {
					references.add(reference);
				}
			}
		}
		
		return IGoal.NO_GOALS;
	}
	
	/*
	 * Caching disabled for now, because the result depends on which ClassType
	 * (as opposed to only the Class) the attribute belongs to.
	 */
	
//	/* casting is safe here */
//	@SuppressWarnings("unchecked")
//	@Override
//	public boolean checkCache() {
//		if (attribute.references != null) {
//			this.references.addAll((List<AttributeReference>) attribute.references);
//			return true;
//		} else {
//			return false;
//		}
//	}
//	
//	@Override
//	public void finish() {
//		attribute.references = this.references;
//	}

}
