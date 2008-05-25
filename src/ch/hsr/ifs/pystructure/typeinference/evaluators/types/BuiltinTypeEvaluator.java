package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;

/**
 * Evaluator for Python built-in types.
 */
public class BuiltinTypeEvaluator extends AbstractEvaluator {

	private final String typeName;
	protected final CombinedType resultType;

	public BuiltinTypeEvaluator(ExpressionTypeGoal goal, String typeName) {
		super(goal);
		
		this.typeName = typeName;
		resultType = goal.resultType;
	}
	
	@Override
	public List<IGoal> init() {
		Class klass = getGoal().getContext().getWorkspace().getBuiltinClass(typeName);
		ClassType classType = new ClassType(klass, null);
		resultType.appendType(classType);

		return IGoal.NO_GOALS;
	}
	
	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		return IGoal.NO_GOALS;
	}

}
