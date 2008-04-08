/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.inferencer;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher.PythonEvaluatorFactory;
import ch.hsr.ifs.pystructure.typeinference.inferencer.logger.IGoalEngineLogger;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class PythonTypeInferencer {

	private final GoalEngine engine;
	
	public PythonTypeInferencer() {
		engine = new GoalEngine(new PythonEvaluatorFactory());
	}
	
	public PythonTypeInferencer(IGoalEngineLogger logger) {
		engine = new GoalEngine(new PythonEvaluatorFactory(), logger);
	}

	public CombinedType evaluateType(Workspace workspace, Module module, SimpleNode node) {
		ModuleContext context = new ModuleContext(workspace, module);
		ExpressionTypeGoal goal = new ExpressionTypeGoal(context, node);
		
		return engine.evaluateGoal(goal);
	}
	
	public void shutdown() {
		engine.shutdown();
	}


}
