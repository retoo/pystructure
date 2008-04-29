package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.MethodResolutionOrderGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;

public class MethodResolutionOrderEvaluator extends AbstractEvaluator {

	private static final class BaseClass {

		public final exprType expr;

		public MetaclassType classType;
		public Linearisation linearization;

		public BaseClass(exprType expr) {
			this.expr = expr;
			this.classType = null;
			this.linearization = null;
		}

		@Override
		public String toString() {
			return "" + expr + " " + classType + " (" + linearization + ")";
		}

	}

	private final Class klass;
	private LinkedList<BaseClass> bases;
	private HashMap<IGoal, BaseClass> index;
	private MethodResolutionOrderGoal goal;

	public MethodResolutionOrderEvaluator(MethodResolutionOrderGoal goal) {
		super(goal);

		this.klass = goal.getKlass();
		this.bases = new LinkedList<BaseClass>();
		this.index = new HashMap<IGoal, BaseClass>();
		this.goal = goal;
	}

	@Override
	public List<IGoal> init() {
		List<IGoal> subgoals = new LinkedList<IGoal>();

		ModuleContext context = new ModuleContext(getGoal().getContext(), klass.getModule());
		for (exprType baseClasseExpression : klass.getBases()) {
			BaseClass baseClass = new BaseClass(baseClasseExpression);

			/* Create and register goal */
			ExpressionTypeGoal expressionTypeGoal = new ExpressionTypeGoal(context, baseClasseExpression);
			subgoals.add(expressionTypeGoal);

			/* Internally register base class */
			this.index.put(expressionTypeGoal, baseClass);
			this.bases.add(baseClass);
		}

		return subgoals;
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		List<IGoal> subgoals = new LinkedList<IGoal>();

		if (state == GoalState.RECURSIVE) {
			throw new RuntimeException("Recursion occured while evaluating MRO of class " + this.klass);
		}
		
		/* In the first phase we resolve all the base classes's type. After that we calculate the MRO */
		if (subgoal instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;

			BaseClass baseClass = index.get(g);
			baseClass.classType  = extractClass(g);

			if (baseClass.classType != null) {
				/* find out resolution */
				MethodResolutionOrderGoal methodResolutionOrderGoal = new MethodResolutionOrderGoal(getGoal().getContext(), baseClass.classType.getKlass());
				subgoals.add(methodResolutionOrderGoal);
				index.put(methodResolutionOrderGoal, baseClass);
			} else {
				/* we are finished here */
			}
		} else if (subgoal instanceof MethodResolutionOrderGoal) {
			MethodResolutionOrderGoal g = (MethodResolutionOrderGoal) subgoal;

			BaseClass baseClass = index.get(g);

			baseClass.linearization = g.linearization;
		} else {
			unexpectedSubgoal(subgoal);
		}

		return subgoals;
	}


	@Override
	public void finish() {
		goal.linearization = calculateLinearsation();

	}

	private Linearisation calculateLinearsation() {
		LinkedList<Linearisation> toMerge = new LinkedList<Linearisation>();

		Linearisation directAnchestors = new Linearisation();

		for (BaseClass baseClass : bases) {
			if (baseClass != null) {
				if (baseClass.classType != null && baseClass.linearization != null) {
					toMerge.add(new Linearisation(baseClass.linearization));
					directAnchestors.add(baseClass.classType.getKlass());
				} else {
					System.err.println("No linearisation for base class: " + baseClass);
				}
			}
		}

		toMerge.add(directAnchestors);

		return Linearisation.merge(this.klass, toMerge);
	}

	private MetaclassType extractClass(ExpressionTypeGoal g) {
		CombinedType result = g.resultType;

		Set<IType> types = result.getTypes();

		if (!types.isEmpty()) {
			if (types.size() > 1) {
				System.err.println("The base class expression " + g.getExpression() + " is not uniquely resolveable");
			}

			/* we just take the first one */
			IType type = types.iterator().next();

			if (type instanceof MetaclassType) {
				return (MetaclassType) type;
			} else {
				System.err.println("Unexpected type for base class expression: " + g.getExpression());
				return null;
			}
		} else {
			/* base class not resolvable at all, we treat this base class as would it not exist */
			return null;
		}
	}

}
