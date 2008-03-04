/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.dltk.goals.GoalState;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.dltk.types.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.ModuleType;
import ch.hsr.ifs.pystructure.typeinference.results.types.PackageType;

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
