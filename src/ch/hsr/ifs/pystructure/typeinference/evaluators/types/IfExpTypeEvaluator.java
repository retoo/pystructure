package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.IfExp;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;

public class IfExpTypeEvaluator extends AbstractEvaluator {
	
	private final IfExp ifExp;
	private CombinedType resultType;

	public IfExpTypeEvaluator(ExpressionTypeGoal goal, IfExp ifExp) {
		super(goal);
		this.ifExp = ifExp;
		
		this.resultType = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		List<IGoal> subgoals = new ArrayList<IGoal>();
		subgoals.add(new ExpressionTypeGoal(getGoal().getContext(), ifExp.body));
		subgoals.add(new ExpressionTypeGoal(getGoal().getContext(), ifExp.orelse));
		return subgoals;
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, GoalState state) {
		ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
		resultType.appendType(g.resultType);
		return IGoal.NO_GOALS;
	}

}
