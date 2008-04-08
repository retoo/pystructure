/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.playground;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class PythonTypeInferencerNG {

	private final ZielMotor engine;
	
	public PythonTypeInferencerNG() {
		engine = new ZielMotor();
	}
	
	public PythonTypeInferencerNG(IGoalEngineLogger logger) {
		engine = new ZielMotor(logger);
	}

	public CombinedType evaluateType(Workspace workspace, Module module, SimpleNode node) {
		ModuleContext context = new ModuleContext(workspace, module);
		ExpressionTypeGoal goal = new ExpressionTypeGoal(context, node);
		
		engine.evaluateGoal(goal);
		
		return goal.resultType;
	}
	
	public void shutdown() {
		engine.shutdown();
	}


}
