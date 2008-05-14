package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.List;

import org.python.pydev.parser.jython.ast.Call;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public abstract class DisplayTypeEvaluator extends AbstractEvaluator {

	protected final CombinedType resultType;

	public DisplayTypeEvaluator(ExpressionTypeGoal goal) {
		super(goal);
		resultType = goal.resultType;
	}
	
	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		return IGoal.NO_GOALS;
	}

	protected void createClassType(String className) {
		Workspace workspace = getGoal().getContext().getWorkspace();
		
		List<Definition> definitions = workspace.getBuiltinModule().getDefinitions(className);
		if (definitions.size() != 1 || !(definitions.get(0) instanceof Class)) {
			throw new RuntimeException("Built-in definition of " + className + " is invalid");
		}
		Class klass = (Class) definitions.get(0);
		
		Call constructorCall = NodeUtils.createFunctionCall(className);
		ClassType classType = new ClassType(klass, constructorCall);
		resultType.appendType(classType);
	}

}
