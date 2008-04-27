/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
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

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.visitors.ImportResolver;

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
		ImportResolver importResolver = getGoal().getContext().getWorkspace().getImportResolver();
		Module fromModule = getGoal().getContext().getModule();
		NamePath path = importDefinition.getPath();
		int level = importDefinition.getLevel();
		PathElement pathElement = importResolver.resolve(fromModule, path, level);
		
		if (pathElement == null) {
			/* this was a module/package which we don't know. Usually 
			 * this is just some sort of external library and it isn't that
			 * bad if we can't import it. Some day we have to generate some warnings here
			 * to let the user know that perhaps his syspath isn't set correctly.
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
		
		if (result instanceof Definition) {
			Definition definition = (Definition) result;
			ModuleContext context = getGoal().getContext();
			if (!(definition instanceof Package)) {
				context = new ModuleContext(context, definition.getModule());
			}
			return wrap(new DefinitionTypeGoal(context, definition));
		} else {
			throw new RuntimeException("Something other than a definition was returned, which should not happen");
		}
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		if (subgoal instanceof DefinitionTypeGoal) {
			DefinitionTypeGoal g = (DefinitionTypeGoal) subgoal;
			this.resultType.appendType(g.resultType);
		} else {
			unexpectedSubgoal(subgoal);
		}
		return IGoal.NO_GOALS;
	}

}
