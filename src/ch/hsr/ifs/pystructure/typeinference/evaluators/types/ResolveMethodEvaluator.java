package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

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
import ch.hsr.ifs.pystructure.typeinference.goals.types.ResolveMethodGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
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
		String methodName = this.methodName;
		
		List<IGoal> subgoals = resolve(classType.getKlass(), classType, methodName);
		
		return subgoals;
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		if (subgoal instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
			
			Set<IType> types = g.resultType.getTypes();
			if (!types.isEmpty()) {
				if (types.size() > 1) {
					System.err.println("The base class expression " + g.getExpression() + " is not uniquely resolveable");
				}
				
				IType type = types.iterator().next();
				
				if (type instanceof MetaclassType) {
					MetaclassType metaClassType = (MetaclassType) type;
					
					return resolve(metaClassType.getKlass(), this.classType, methodName);
				} else {
					System.err.println("Unexpected type for base class expression: " + g.getExpression());
				}
			} else {
				/* base class not resolvable at all */
			}
		} else {
			unexpectedSubgoal(subgoal);
		}
		return IGoal.NO_GOALS;
	}
	
	private List<IGoal> resolve(Class klass, ClassType classType, String methodName) {
		Method method = klass.getMethod(methodName);

		if (method != null) {
			this.resultType.appendType(new MethodType(klass.getModule(), method, classType));
			
			return IGoal.NO_GOALS;
		} else {	
			ModuleContext context = new ModuleContext(getGoal().getContext(), klass.getModule());
			
			List<exprType> baseClasses = klass.getBases();
			if (baseClasses.size() != 0) {
				if (baseClasses.size() == 1) {
					/* nice, only one S */
				} else {
					System.err.println("Warning, more than one base class for class " + klass 
									+   ", currenlty only one is supported");
				}
				return wrap(new ExpressionTypeGoal(context, baseClasses.get(0)));
			} else {
				/* no base class */
				return IGoal.NO_GOALS;
			}
		}
	}

}
