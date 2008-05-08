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

package ch.hsr.ifs.pystructure.typeinference.evaluators.references;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

/**
 * Evaluator for finding uses of a name, which could be possible references to a
 * function or method.
 */
public class PossibleReferencesEvaluator extends AbstractEvaluator {

	private String name;
	
	private List<Use> uses;

	public PossibleReferencesEvaluator(PossibleReferencesGoal goal) {
		super(goal);
		name = goal.getName();
		
		uses = goal.references;
	}

	@Override
	public List<IGoal> init() {
		ModuleContext context = getGoal().getContext();
		Workspace workspace = context.getWorkspace();
		
		for (Module module : workspace.getModules()) {
			for (Use use : module.getContainedUses()) {
				if (use.getName().equals(name)) {
					uses.add(use);
				}
			}
		}
		return IGoal.NO_GOALS;
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		return IGoal.NO_GOALS;
	}

}
