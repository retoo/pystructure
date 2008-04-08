/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
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
		this.attributeName = goal.getAttributeName();
		this.attributeParent = goal.getAttributeParent();
		
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
