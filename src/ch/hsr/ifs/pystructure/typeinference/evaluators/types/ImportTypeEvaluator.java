/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElementContainer;
import ch.hsr.ifs.pystructure.typeinference.results.types.FunctionType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.ModuleType;
import ch.hsr.ifs.pystructure.typeinference.results.types.PackageType;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

/**
 * Evaluator for the type of an import, which could be a module, a class, a
 * function, or a global variable.
 */
public class ImportTypeEvaluator extends DefinitionTypeEvaluator {
	
	private final ImportDefinition importDefinition;
	
	public ImportTypeEvaluator(DefinitionTypeGoal goal, ImportDefinition importDefinition) {
		super(goal, importDefinition);
		this.importDefinition = importDefinition;
	}

	@Override
	public List<IGoal> init() {
		NamePath path = importDefinition.getPath();
		PathElement pathElement = resolve(path, importDefinition.getLevel());
		
		if (pathElement == null) {
			/* this was a module/package which we don't know. Usually 
			 * this is just some sort of external library and it isn't that
			 * bad if we can't import it. Some day we have to generate some warnings here
			 * to let the user know that perhaps his syspath isnt set correctly.
			 */
			return IGoal.NO_GOALS;
		}
		
		Object result;
		
		if (importDefinition.getElement() == null) {
			result = pathElement;
		} else {
			if (pathElement instanceof Module) {
				Module module = (Module) pathElement;
				result = module.getChild(importDefinition.getElement());
			} else if (pathElement instanceof Package) {
				Package pkg = (Package) pathElement;
				result = pkg.getChild(importDefinition.getElement());
			} else {
				throw new RuntimeException("ImportFrom doesn't import from a package or module");
			}
		}
		
		if (result instanceof Package) {
			Package pkg = (Package) result;
			resultType.appendType(new PackageType(pkg));
		} else if (result instanceof Module) {
			Module module = (Module) result;
			resultType.appendType(new ModuleType(module));
		} else if (result instanceof Class) {
			Class klass = (Class) result;
			resultType.appendType(new MetaclassType(klass.getModule(), klass));
		} else if (result instanceof Function) {
			Function function = (Function) result;
			resultType.appendType(new FunctionType(function.getModule(), function));
		}

		return IGoal.NO_GOALS;
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		return IGoal.NO_GOALS;
	}

	private PathElement resolve(NamePath path, int level) {
		/* first we try to look if it is a relative lookup*/
		Workspace workspace = getGoal().getContext().getWorkspace();
		Module module = getGoal().getContext().getModule();
		
		PathElementContainer parent = module.getParent();

		boolean isRelativeImport = (level != 0);
		
		if (isRelativeImport && level > 1) {
			for (int i = 1; i < level; i++) {
				if (parent == null) {
					throw new RuntimeException("Relative Invalid relative import.");
				}
				parent = parent.getParent();
			}
		}
		
		if (isRelativeImport && !(parent instanceof Package)) {
			throw new RuntimeException("Relative import not inside package");
		}
		
		PathElement result = workspace.resolve(path, parent);
		if (result != null) {
			return result;
		}

		if (isRelativeImport) {
			throw new RuntimeException("Invalid relative import.");
		}
		
		/* Search absolute in all source folders */
		result = workspace.resolve(path);
		
		/* If result is null, the import failed. A warning might be useful here. */
		
		return result;
	}

}
