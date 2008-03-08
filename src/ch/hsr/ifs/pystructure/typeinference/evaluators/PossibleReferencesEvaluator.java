/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.PythonContext;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.GoalState;
import ch.hsr.ifs.pystructure.typeinference.dltk.goals.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Use;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

/**
 * Evaluator for finding uses of a name, which could be possible references to a
 * function or method.
 */
public class PossibleReferencesEvaluator extends PythonEvaluator {

	private NameAdapter name;
	
	private List<Use> uses;

	public PossibleReferencesEvaluator(PossibleReferencesGoal goal) {
		super(goal);
		name = goal.getName();
		
		uses = new ArrayList<Use>();
	}

	@Override
	public List<IGoal> init() {
		PythonContext context = getGoal().getContext();
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
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		return IGoal.NO_GOALS;
	}

	@Override
	public Object produceResult() {
		return uses;
	}

}