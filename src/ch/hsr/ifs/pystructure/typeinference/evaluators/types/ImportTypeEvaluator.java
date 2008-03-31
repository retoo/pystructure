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
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.IPackage;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportPath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Path;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.ModuleType;
import ch.hsr.ifs.pystructure.typeinference.results.types.PackageType;

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
		String path = importDefinition.getPath();
		Path parent = resolve(path, importDefinition.getLevel());
		
		if (parent == null) {
			/* this was a module/package which we don't know. Usually 
			 * this is just some sort of external library and it isn't that
			 * bad if we can't import it. Some day we have to generate some warnings here
			 * to let the user know that perhaps his syspath isnt set correctly.
			 */
			return IGoal.NO_GOALS;
		}
		
		PathElement pathElement = parent.top();
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
		}

		return IGoal.NO_GOALS;
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, GoalState state) {
		return IGoal.NO_GOALS;
	}

	private Path resolve(String moduleName, int level) {
		/* first we try to look if it is a relative lookup*/
		Module module = getGoal().getContext().getModule();
		IPackage parentPath = module.getPackage(); 

		if (level > 1) {
			for (int i = 1; i < level; i++) {
				parentPath = parentPath.getParent();
			}
		}
		
		Path found = parentPath.lookFor(moduleName);

		if (found != null) {
			return found;
		}

		/* second, if we haven't found anything yet, walk through the sys.path */
		List<ImportPath> importPaths = getGoal().getContext().getWorkspace().getImportPaths();
		for (ImportPath importPath : importPaths) {
			found = importPath.lookFor(moduleName);

			if (found != null) {
				return found;
			}
		}
		
		// Probably from Python standard library or built-in.
		// System.err.println("Warning: Unable to find " + moduleName + " imported by " + module.getPath());
		return null;
	}

}
