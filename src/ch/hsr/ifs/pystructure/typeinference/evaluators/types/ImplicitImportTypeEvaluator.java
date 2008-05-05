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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImplicitImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportPath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.visitors.ImportResolver;

/**
 * For evaluating the type of implicit imports like "from module import *" and
 * built-in types.
 * 
 * It first tries all import star paths before it resorts to looking up the
 * definition as a built-in.
 */
public class ImplicitImportTypeEvaluator extends DefinitionTypeEvaluator {
	
	private final ImplicitImportDefinition implicitImportDefinition;

	/**
	 * Working list of import star paths for searching the definition.
	 */
	private final LinkedList<ImportPath> importStarPaths;
	
	private boolean tryingBuiltin = false;
	
	public ImplicitImportTypeEvaluator(DefinitionTypeGoal goal, ImplicitImportDefinition implicitImportDefinition) {
		super(goal, implicitImportDefinition);
		this.implicitImportDefinition = implicitImportDefinition;

		this.importStarPaths = new LinkedList<ImportPath>(implicitImportDefinition.getImportStarPaths());
	}

	@Override
	public List<IGoal> init() {
		return tryNextImportStarPath();
	}
	
	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		if (subgoal instanceof DefinitionTypeGoal) {
			DefinitionTypeGoal g = (DefinitionTypeGoal) subgoal;
			
			Set<IType> types = g.resultType.getTypes();
			if (!types.isEmpty()) {
				/* There were results, so look no further */
				resultType.appendType(g.resultType);
			} else {
				/* There were no results, so continue looking */
				return tryNextImportStarPath();
			}
			
		} else {
			unexpectedSubgoal(subgoal);
		}
		
		return IGoal.NO_GOALS;
	}

	/**
	 * Look at the next import star path and generate subgoals or add more
	 * import star paths to search the definition.
	 */
	private List<IGoal> tryNextImportStarPath() {
		List<IGoal> subgoals = new LinkedList<IGoal>();
		
		if (!importStarPaths.isEmpty()) {
			ImportResolver importResolver = getGoal().getContext().getWorkspace().getImportResolver();
			ImportPath importStarPath = importStarPaths.removeFirst();
			PathElement parent = importResolver.resolve(importStarPath);
			
			List<Definition> definitions;
			if (parent instanceof Module) {
				Module module = (Module) parent;
				definitions = module.getDefinitions(implicitImportDefinition.getName());
			} else if (parent instanceof Package) {
				throw new RuntimeException("Importing definitions from __init__.py not yet implemented");
			} else {
				throw new RuntimeException("Import doesn't import from a package or module: " + importStarPath);
			}
			
			for (Definition definition : definitions) {
				if (definition instanceof ImplicitImportDefinition) {
					ImplicitImportDefinition def = (ImplicitImportDefinition) definition;
					importStarPaths.addAll(0, def.getImportStarPaths());
				} else {
					ModuleContext context = new ModuleContext(getGoal().getContext(), definition.getModule());
					subgoals.add(new DefinitionTypeGoal(context, definition));
				}
			}
		}
		
		if (!subgoals.isEmpty()) {
			return subgoals;
		} else if (!importStarPaths.isEmpty()) {
			/* Maybe we could do this with a loop instead of recursing. */
			return tryNextImportStarPath();
		} else if (!tryingBuiltin) {
			tryingBuiltin = true;
			Module builtinModule = getGoal().getContext().getWorkspace().getBuiltinModule();
			List<Definition> definitions = builtinModule.getDefinitions(implicitImportDefinition.getName());
			for (Definition definition : definitions) {
				if (definition instanceof ImplicitImportDefinition) {
					// Ignore an implicit import definition because we're
					// already trying the built-ins.
					continue;
				}
				ModuleContext context = new ModuleContext(getGoal().getContext(), builtinModule);
				subgoals.add(new DefinitionTypeGoal(context, definition));
			}
			return subgoals;
		} else {
			// Really no definition found.
			return IGoal.NO_GOALS;
		}
	}
	
}
