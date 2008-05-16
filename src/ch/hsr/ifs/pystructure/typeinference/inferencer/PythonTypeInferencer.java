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

package ch.hsr.ifs.pystructure.typeinference.inferencer;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.misc.MethodResolutionOrderGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.MethodResolutionOrder;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;


public class PythonTypeInferencer {

	private final GoalEngine engine;
	
	public PythonTypeInferencer() {
		engine = new GoalEngine();
	}
	
	public PythonTypeInferencer(IGoalEngineLogger logger) {
		engine = new GoalEngine(logger);
	}

	public CombinedType evaluateType(Workspace workspace, Module module, SimpleNode node) {
		ModuleContext context = new ModuleContext(workspace, module);
		ExpressionTypeGoal goal = new ExpressionTypeGoal(context, node);
		
		engine.evaluateGoal(goal);
		return goal.resultType;
	}
	
	public MethodResolutionOrder getMRO(Workspace workspace, Module module, Class klass) {
		ModuleContext context = new ModuleContext(workspace, module);
		MethodResolutionOrderGoal goal = new MethodResolutionOrderGoal(context, klass);
		
		engine.evaluateGoal(goal);
		
		return goal.linearization;
	}
	
	
	public void shutdown() {
		engine.shutdown();
	}


}
