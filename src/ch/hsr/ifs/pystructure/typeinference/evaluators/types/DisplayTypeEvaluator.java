package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.List;

import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.exprType;

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

/**
 * Evaluator for Python displays. Displays are somewhat like literals, but the
 * difference is that a display results in a new object every time. Literals, on
 * the other hand, return the same objects for the same literals. Or, in other
 * (pythonic) words:
 * 
 * >>> "test" is "test"
 * True
 * >>> [1, 2] is [1, 2]
 * False
 * 
 * There are list and dict displays. The initial values need to be processed by
 * the evaluator and this is what this base class is for.
 */
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

	protected void createClassType(String className, exprType display) {
		Workspace workspace = getGoal().getContext().getWorkspace();
		
		List<Definition> definitions = workspace.getBuiltinModule().getDefinitions(className);
		if (definitions.size() != 1 || !(definitions.get(0) instanceof Class)) {
			throw new RuntimeException("Built-in definition of " + className + " is invalid");
		}
		Class klass = (Class) definitions.get(0);
		
		Call constructorCall = NodeUtils.createFunctionCall(className);
		constructorCall.beginLine = display.beginLine;
		
		ClassType classType = new ClassType(klass, constructorCall);
		resultType.appendType(classType);
	}

}
