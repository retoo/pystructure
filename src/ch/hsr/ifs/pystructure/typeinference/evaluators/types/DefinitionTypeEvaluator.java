/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
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

package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;

/**
 * The {@link DefinitionTypeEvaluator} is the base class for several evaluators
 * which are responsible for finding out the type of a given definition.
 * 
 * The sub evaluators are:
 *   {@link ArgumentTypeEvaluator}: Type of arguments
 *   {@link AssignTypeEvaluator}: Type of the value in a assignment
 *   {@link ImplicitImportTypeEvaluator}: Type of implicitly imported  
 *        stuff (from .. import * or python built ins)
 *   {@link ImportTypeEvaluator}: Type of a imported object
 *         (class, variable or function)  
 * 
 * The {@link DefinitionTypeEvaluator} handles the caching for all sub 
 * classes. Once resolved definitions are not resolved another time. Please 
 * note that some sub classes disable this behaviour in favour of better 
 * analysing techniques (namely: call context and instance context)
 */
public abstract class DefinitionTypeEvaluator extends AbstractEvaluator {

	private Definition definition;
	protected CombinedType resultType;
	
	public DefinitionTypeEvaluator(DefinitionTypeGoal goal, Definition definition) {
		super(goal);
		this.definition = definition;
		this.resultType = goal.resultType;
	}
	
	@Override
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
