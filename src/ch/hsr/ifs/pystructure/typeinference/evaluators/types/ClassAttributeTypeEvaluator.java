package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Assign;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.AttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ClassAttributeTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;

public class ClassAttributeTypeEvaluator extends GoalEvaluator {

	private ClassType classType;
	private NameAdapter attributeName;
	private CombinedType resultType;
	private Class klass;

	public ClassAttributeTypeEvaluator(ClassAttributeTypeGoal goal) {
		super(goal);

		this.classType = goal.getClassType();
		this.klass = classType.getKlass();
		this.attributeName = goal.getAttributeName();

		this.resultType = new CombinedType();
	}

	@Override
	public List<IGoal> init() {
		// It's probably a data attribute
		return wrap(
				new AttributeReferencesGoal( getGoal().getContext(),
						attributeName, klass));
	}
	
	
	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		ArrayList<IGoal> subgoals = new ArrayList<IGoal>();
		
		if (subgoal instanceof AttributeReferencesGoal) {
			/* there's no way to */
			@SuppressWarnings("unchecked")
			List<AttributeReference> references = (List<AttributeReference>) result;

			for (AttributeReference reference : references) {
				SimpleNode node = reference.getNode();
				if (node.parent instanceof Assign) {
					Assign assign = (Assign) node.parent;
					// TODO: Tuples
					if (assign.targets[0] == node) {
						// The ExpressionTypeGoal has to be evaluated in the
						// context of the reference (its module)
						ModuleContext context = new ModuleContext(getGoal().getContext(), reference.getModule());
						subgoals.add(new ExpressionTypeGoal(context, assign.value));
					} else {
						/* skip .. ? */	
					}
				} /* skip all non-assign nodes */
			}
		} else if (subgoal instanceof ExpressionTypeGoal) {
			IEvaluatedType type = (IEvaluatedType) result;
			resultType.appendType(type);
		} else {
			throw new RuntimeException("Unknown subgoal");
		}

		return subgoals;
	}
	
	@Override
	public boolean isCached() {
		CombinedType cachedType = klass.attributes.get(attributeName);
		if (cachedType != null) {
			resultType = cachedType;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object produceResult() {
		if (klass.attributes.get(attributeName) == null) {
			klass.attributes.put(attributeName, resultType);
		}
		
		return resultType;
	}

}
