package ch.hsr.ifs.pystructure.typeinference.evaluators.references;

import java.util.LinkedList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.misc.MethodResolutionOrderGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.CalculateTypeHierarchyGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class CalculateTypeHierarchyEvaluator extends AbstractEvaluator {

	private Workspace workspace;

	public CalculateTypeHierarchyEvaluator(CalculateTypeHierarchyGoal goal) {
		super(goal);

		this.workspace = goal.getWorkspace();
	}

	@Override
	public List<IGoal> init() {
		LinkedList<IGoal> subgoals = new LinkedList<IGoal>();

		ModuleContext parentContext = getGoal().getContext();

		for (Module module : workspace.getModules()) {
			ModuleContext context = new ModuleContext(parentContext, module);

			for (Class klass : module.getClasses()) {
				subgoals.add(new MethodResolutionOrderGoal(context, klass));
			}
		}

		return subgoals;
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		if (subgoal instanceof MethodResolutionOrderGoal) {
			MethodResolutionOrderGoal g = (MethodResolutionOrderGoal) subgoal;

			Class klass = g.getKlass();

			boolean skipSelf = false;
			
			for (Class baseClasses : klass.getLinearization()) {
				if (klass == baseClasses) {
					skipSelf = true;
					continue;
				}
				
				baseClasses.addSubClass(klass);
			}

			if (!skipSelf) {
				/* sanity check, ensure that the klass itself was
				 * encountered in the linearisation
				 */
				throw new RuntimeException("Didn't see the klass " + klass + " in its own linearisation");
			}
		}

		return IGoal.NO_GOALS;
	}

}
