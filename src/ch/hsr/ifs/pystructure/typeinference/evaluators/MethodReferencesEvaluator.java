/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.ClassReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.MethodReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleAttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.results.references.ClassReference;
import ch.hsr.ifs.pystructure.typeinference.results.references.MethodReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;

/**
 * Evaluator for finding all the references of a method. This is done in two
 * stages. First, find all possible references, then validate whether the
 * receiver is of the right type.
 */
public class MethodReferencesEvaluator extends PythonEvaluator {

	private final Method method;
	
	private List<MethodReference> references;
	
	public MethodReferencesEvaluator(MethodReferencesGoal goal) {
		super(goal);
		this.method = goal.getMethod();
		
		this.references = new ArrayList<MethodReference>();
	}

	@Override
	public List<IGoal> init() {
		ModuleContext context = getGoal().getContext();
		
		if (isConstructor()) {
			return wrap(new ClassReferencesGoal(context, method.getKlass()));
		} else {
			return wrap(new PossibleAttributeReferencesGoal(context, method.getName()));
		}
	}
	
	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		
		if (subgoal instanceof ClassReferencesGoal) {
			// We were looking for a constructor.
			
			List<ClassReference> classReferences = (List<ClassReference>) result;
			for (ClassReference classReference : classReferences) {
				references.add(new MethodReference(method, classReference.getNode(), true));
			}
		}
		
		if (subgoal instanceof PossibleAttributeReferencesGoal) {
			// We were looking for a normal method.
			
			List<AttributeReference> attributeReferences = (List<AttributeReference>) result;
			
			for (AttributeReference reference : attributeReferences) {
				SimpleNode attribute = reference.getNode();
				
				for (IEvaluatedType parentType : EvaluatorUtils.extractTypes(reference.getParent())) {
					if (parentType instanceof ClassType) {
						ClassType classType = (ClassType) parentType;
						if (classType.getKlass() != null && classType.getKlass().equals(method.getKlass())) {
							references.add(new MethodReference(method, attribute));
						}
					}
					if (parentType instanceof MetaclassType) {
						MetaclassType metaclassType = (MetaclassType) parentType;
						if (metaclassType.getKlass().equals(method.getKlass())) {
							references.add(new MethodReference(method, attribute, false));
						}
					}

				}
			}
		}
		
		return IGoal.NO_GOALS;
	}

	@Override
	public Object produceResult() {
		return references;
	}

	private boolean isConstructor() {
		return method.getName().getId().equals("__init__");
	}

}
