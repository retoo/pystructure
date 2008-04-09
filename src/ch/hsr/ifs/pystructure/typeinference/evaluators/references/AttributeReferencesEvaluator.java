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
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.ModuleType;

/**
 * Evaluator for finding all the references to a data attribute of a class.
 */
public class AttributeReferencesEvaluator extends AbstractEvaluator {

	private final String attributeName;
	private final Definition attributeParent;
	
	private List<AttributeReference> references;
	private Attribute attribute;
	
	public AttributeReferencesEvaluator(AttributeReferencesGoal goal) {
		super(goal);
		this.attribute = goal.getAttribute();
		this.attributeName = attribute.getName();
		this.attributeParent = attribute.getKlass();
		
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
				if (type instanceof ClassType) {
					checkPossibleReference(reference, ((ClassType) type).getKlass());
				} else if (type instanceof ModuleType) {
					checkPossibleReference(reference, ((ModuleType) type).getModule());
				} else if (type instanceof MetaclassType) {
					checkPossibleReference(reference, ((MetaclassType) type).getKlass());
				}
			}
		}
		
		return IGoal.NO_GOALS;
	}
	
	/* casting is safe here */
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkCache() {
		if (attribute.references != null) {
			this.references.addAll((List<AttributeReference>) attribute.references);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void finish() {
		attribute.references = this.references;
	}

	private void checkPossibleReference(AttributeReference reference, Definition attributeParent) {
		if (this.attributeParent.equals(attributeParent)) {
			references.add(reference);
		}
	}

}
