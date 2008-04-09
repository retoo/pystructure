/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;

public abstract class DefinitionTypeEvaluator extends AbstractEvaluator {

	private Definition definition;
	protected CombinedType resultType;
	
	public DefinitionTypeEvaluator(DefinitionTypeGoal goal, Definition definition) {
		super(goal);
		this.definition = definition;
		this.resultType = goal.resultType;
	}
	
	public boolean checkCache() {
		if (definition.type != null) {
			this.resultType.appendType(definition.type);
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void finish() {
		definition.type = this.resultType;
	}
	
}
