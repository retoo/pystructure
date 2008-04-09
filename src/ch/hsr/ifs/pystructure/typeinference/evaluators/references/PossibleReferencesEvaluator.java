/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
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
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		return IGoal.NO_GOALS;
	}

}
