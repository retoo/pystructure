package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.MethodResolutionOrderGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ResolveMethodGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MethodType;

public class ResolveMethodEvaluator extends AbstractEvaluator {

	private ClassType classType;
	private String methodName;
	
	private CombinedType resultType;

	public ResolveMethodEvaluator(ResolveMethodGoal goal) {
		super(goal);
		
		this.classType = goal.getClassType();
		this.methodName = goal.getAttributeName();
		
		this.resultType = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		Class klass = this.classType.getKlass();
		
		if (klass != null) {
			return wrap(new MethodResolutionOrderGoal(getGoal().getContext(), klass));
		} else {
			System.err.println("Class is null for class type " + this.classType);
			
			return IGoal.NO_GOALS;
		}
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		if (subgoal instanceof MethodResolutionOrderGoal) {
			MethodResolutionOrderGoal g = (MethodResolutionOrderGoal) subgoal;

			for (Class klass : g.linearization) {
				Method method = klass.getMethod(methodName);
				
				if (method != null) {
					this.resultType.appendType(new MethodType(klass.getModule(), method, classType));
					break;
				}
			}
		} else {
			unexpectedSubgoal(subgoal);
		}
		
		return IGoal.NO_GOALS;
	}
	
}
