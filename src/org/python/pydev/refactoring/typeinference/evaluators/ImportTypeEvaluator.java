/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.evaluators;

import java.util.List;

import org.python.pydev.refactoring.typeinference.dltk.goals.GoalState;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;
import org.python.pydev.refactoring.typeinference.goals.types.DefinitionTypeGoal;
import org.python.pydev.refactoring.typeinference.model.definitions.Class;
import org.python.pydev.refactoring.typeinference.model.definitions.ImportDefinition;
import org.python.pydev.refactoring.typeinference.model.definitions.Module;
import org.python.pydev.refactoring.typeinference.model.definitions.Package;
import org.python.pydev.refactoring.typeinference.results.types.MetaclassType;
import org.python.pydev.refactoring.typeinference.results.types.ModuleType;
import org.python.pydev.refactoring.typeinference.results.types.PackageType;

/**
 * Evaluator for the type of an import, which could be a module, a class, a
 * function, or a global variable.
 */
public class ImportTypeEvaluator extends PythonEvaluator {
	
	private final ImportDefinition importDefinition;
	private IEvaluatedType type;
	
	public ImportTypeEvaluator(DefinitionTypeGoal goal, ImportDefinition importDefinition) {
		super(goal);
		this.importDefinition = importDefinition;
	}

	@Override
	public List<IGoal> init() {
		Object element = importDefinition.getElement();
		
		if (element instanceof Package) {
			Package pkg = (Package) element;
			type = new PackageType(pkg);
		} else if (element instanceof Module) {
			Module module = (Module) element;
			type = new ModuleType(module);
		} else if (element instanceof Class) {
			Class klass = (Class) element;
			type = new MetaclassType(klass.getModule(), klass);
		}

		return IGoal.NO_GOALS;
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		return IGoal.NO_GOALS;
	}

	@Override
	public Object produceResult() {
		return type;
	}
}
