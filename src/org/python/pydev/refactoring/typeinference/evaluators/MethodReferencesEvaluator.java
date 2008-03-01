/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.dltk.goals.GoalState;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;
import org.python.pydev.refactoring.typeinference.goals.references.ClassReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.references.MethodReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.references.PossibleAttributeReferencesGoal;
import org.python.pydev.refactoring.typeinference.model.definitions.Method;
import org.python.pydev.refactoring.typeinference.results.references.AttributeReference;
import org.python.pydev.refactoring.typeinference.results.references.ClassReference;
import org.python.pydev.refactoring.typeinference.results.references.MethodReference;
import org.python.pydev.refactoring.typeinference.results.types.ClassType;
import org.python.pydev.refactoring.typeinference.results.types.MetaclassType;

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
		PythonContext context = getGoal().getContext();
		
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
