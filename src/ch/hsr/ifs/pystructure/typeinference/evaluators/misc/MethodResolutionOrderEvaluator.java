package ch.hsr.ifs.pystructure.typeinference.evaluators.misc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.MethodResolutionOrderGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.MethodResolutionOrder;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;

/**
 * The {@link MethodResolutionOrderEvaluator} calculates the method resolution
 * order (MRO) of a given class. The MRO defines in which order the interpreter has
 * to look for inherited methods or attributes. 
 * 
 * The algorithm to calculate the MRO is exactly the same as used in the original 
 * Python interpreteter (as of Python 2.3) and provides some special properties 
 * in regards of multiple inheritance. 
 * 
 * Briefly described (TODO tönt das gut) the algorithm works as follows: 
 *  1. The final result of the algorithm is a list of classes describing the MRO 
 *     (also called linearisation) of a given class 
 *  2. The MRO of a single class is just the class itself: class C() --> L(C) = [C]
 *  3. When a class inherits only from a single class the MRO are both involved class, 
 *     with the 'lower' class first: class S(B) --> L(S) = [S,B]
 *  4. The MRO of classes with multiple base classes has to be calculated using a 
 *     function called usually merge. The MRO of all base classes and the direct 
 *     base classes have to be passed to the merge functions. 
 *       class A(B, C) -> L(A) = [A] + merge(L(B), L(C), [B, C])
 *  5. The merge works as follows:
 *     a) all passed arguments are lists of classes, we call the first element
 *        the head, and the remaining elements the tail. 
 *     b) iterate over all lists and look for a tail which doesn't occur in any of
 *        the existing tails. If there is such a head, remove the class stored in this
 *        head (and other heads, if there are any) and add it to the result linearisation
 *     c) repeat step b as long as there are any classes remaining. If the engine can't 
 *        find a head which doesn't occur in any tail the process has to be aborted, the 
 *        specified class hierarchy is cannot be resolved into a valid linearisation. 
 *   
 * More details about the algorithm and the details behind it can be found in the document 
 * 'The Python 2.3 Method Resolution Order' by Michele Simionato (http://www.python.org/download/releases/2.3/mro/)
 * 
 * At the beginning the evaluator just knows the base classe by their names. To find find the corresponding classes
 * it first issues a {@link ExpressionTypeGoal} for every base class. After doing so it issues a {@link MethodResolutionOrderGoal} 
 * for every fond base class. When the MRO of all base classes has been caluclated the evaluator continues to calculate MRO 
 * of the class in question. 
 * 
 */
public class MethodResolutionOrderEvaluator extends AbstractEvaluator {

	private static final class BaseClass {

		public final exprType expr;

		public MetaclassType classType;
		public MethodResolutionOrder linearization;

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
		for (exprType baseClasseExpression : klass.getBaseClasses()) {
			/* Create and register goal */
			ExpressionTypeGoal expressionTypeGoal = new ExpressionTypeGoal(context, baseClasseExpression);
			subgoals.add(expressionTypeGoal);

			/* Internally register base class */
			BaseClass baseClass = new BaseClass(baseClasseExpression);
			this.index.put(expressionTypeGoal, baseClass);
			this.bases.add(baseClass);
		}

		return subgoals;
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		List<IGoal> subgoals = new LinkedList<IGoal>();

		if (subgoalState == GoalState.RECURSIVE) {
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
	public boolean checkCache() {
		MethodResolutionOrder linearization = klass.getLinearization();
		
		if (linearization != null) {
			goal.linearization = linearization;
			return true;
		} else {
			return false;
		}
	}


	@Override
	public void finish() {
		if (goal.linearization == null) {  
			goal.linearization = calculateLinearsation();
		}
		
		if (klass.getLinearization() == null) {
			klass.setLinearization(goal.linearization);
		}
	}

	/**
	 * Initiates the algorithms used to calculate the linearisation.
	 * 
	 * @return 
	 */
	private MethodResolutionOrder calculateLinearsation() {
		LinkedList<MethodResolutionOrder> toMerge = new LinkedList<MethodResolutionOrder>();

		MethodResolutionOrder directAnchestors = new MethodResolutionOrder();

		for (BaseClass baseClass : bases) {
			if (baseClass.classType != null) {
				if (baseClass.linearization != null) {
					toMerge.add(new MethodResolutionOrder(baseClass.linearization));
					directAnchestors.add(baseClass.classType.getKlass());
				} else {
					System.err.println("No linearisation for base class: " + baseClass);
				}
			}
		}

		toMerge.add(directAnchestors);

		return MethodResolutionOrder.merge(this.klass, toMerge);
	}

	/**
	 * Extracts the {@link MetaclassType} out of the given
	 * {@link ExpressionTypeGoal}. The method reports problems with the goal,
	 * for example when there is more than one class reported for the expression
	 * (which shouldn't actually happen) or if the a different type than MetaclassType is being 
	 * reported.  
	 * 
	 * So this function just does some casting. 
	 * 
	 * @param goal goal from which the metaclass type has to be extract from
	 * @return actual {@link MetaclassType}
	 */
	private static MetaclassType extractClass(ExpressionTypeGoal goal) {
		Set<IType> types = goal.resultType.getTypes();

		if (!types.isEmpty()) {
			if (types.size() > 1) {
				System.err.println("The base class expression " + goal.getExpression() + " is not uniquely resolveable");
			}

			/* we just take the first one */
			IType type = types.iterator().next();

			if (type instanceof MetaclassType) {
				return (MetaclassType) type;
			} else {
				System.err.println("Unexpected type for base class expression: " + goal.getExpression());
				return null;
			}
		} else {
			/* base class not resolvable at all, we treat this base class as would it not exist */
			return null;
		}
	}

}
