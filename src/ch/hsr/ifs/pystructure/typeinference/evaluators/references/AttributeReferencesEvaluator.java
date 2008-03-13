/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.references;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.EvaluatorUtils;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.PythonEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.AttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleAttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.ModuleType;

/**
 * Evaluator for finding all the references to a data attribute of a class.
 */
public class AttributeReferencesEvaluator extends PythonEvaluator {

	private final NameAdapter attributeName;
	private final Definition attributeParent;
	
	private List<AttributeReference> references;
	
	public AttributeReferencesEvaluator(AttributeReferencesGoal goal) {
		super(goal);
		this.attributeName = goal.getAttributeName();
		this.attributeParent = goal.getAttributeParent();
		
		this.references = new ArrayList<AttributeReference>();
	}

	@Override
	public List<IGoal> init() {
		return wrap(new PossibleAttributeReferencesGoal(getGoal().getContext(), attributeName));
	}
	
	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		List<AttributeReference> possibleReferences = (List<AttributeReference>) result;
		
		for (AttributeReference reference : possibleReferences) {
			for (IEvaluatedType type : EvaluatorUtils.extractTypes(reference.getParent())) {
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

	@Override
	public Object produceResult() {
		return references;
	}

	private void checkPossibleReference(AttributeReference reference, Definition attributeParent) {
		if (this.attributeParent.equals(attributeParent)) {
			references.add(reference);
		}
	}

}
