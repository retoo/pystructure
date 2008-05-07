/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.references;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.contexts.InstanceContext;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.ClassReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.MethodReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleAttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ResolveMethodGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Reference;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.results.references.ClassReference;
import ch.hsr.ifs.pystructure.typeinference.results.references.ConstructorReference;
import ch.hsr.ifs.pystructure.typeinference.results.references.FunctionReference;
import ch.hsr.ifs.pystructure.typeinference.results.references.MethodReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MethodType;

/**
 * Evaluator for finding all the references of a method. This is done in two
 * stages. First, find all possible references, then validate whether the
 * receiver is of the right type.
 */
public class MethodReferencesEvaluator extends AbstractEvaluator {

	private final Method method;
	
	private List<FunctionReference> references;
	private HashMap<IGoal, List<Reference>> possibleReferences;

	private ModuleContext context;
	
	public MethodReferencesEvaluator(MethodReferencesGoal goal) {
		super(goal);
		this.method = goal.getMethod();
		
		
		this.context = goal.getContext();
		this.references = goal.references;
		this.possibleReferences = new HashMap<IGoal, List<Reference>>();
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
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		List<IGoal> subgoals = new LinkedList<IGoal>();
		
		if (subgoal instanceof ClassReferencesGoal) {
			ClassReferencesGoal g = (ClassReferencesGoal) subgoal;
			
			for (ClassReference reference : g.references) {
				MetaclassType metaClassType = reference.getMetaclassType();
				resolveMethod(reference, metaClassType, subgoals);
			}
			
		} else 	if (subgoal instanceof PossibleAttributeReferencesGoal) {
			PossibleAttributeReferencesGoal g = (PossibleAttributeReferencesGoal) subgoal; 
			
			
			// We were looking for a normal method.
			for (AttributeReference reference : g.references) {
				exprType attribute = reference.getExpression();
				Module module = reference.getModule();
				Class wantedClass = method.getKlass();
				
				for (IType parentType : reference.getParent()) {
						
					if (parentType instanceof MetaclassType) {
						MetaclassType metaclassType = (MetaclassType) parentType;
						if (metaclassType.getKlass().equals(method.getKlass())) {
							references.add(new MethodReference(method, attribute, module, false));
						}
					
					} else if (parentType instanceof ClassType) {
						ClassType referenceClassType = (ClassType) parentType;
						boolean referenceValid = false;
						
						InstanceContext instanceContext = context.getInstanceContext();
						if (instanceContext != null && wantedClass.equals(instanceContext.getClassType().getKlass()))  {
							// InstanceContext applies
							referenceValid = referenceClassType.equals(instanceContext.getClassType());
						} else {
							resolveMethod(reference, referenceClassType, subgoals);
						}
						
						if (referenceValid) {
							references.add(new MethodReference(method, attribute, module));
						}
					}
				}
			}
			
		} else if (subgoal instanceof ResolveMethodGoal) {
			ResolveMethodGoal g = (ResolveMethodGoal) subgoal;
			List<Reference> referencesList = possibleReferences.get(g);
			for (MethodType methodType : g.methodTypes) {
				Method methodCandidate = methodType.getMethod();

				if (method.equals(methodCandidate)) {
					for (Reference reference : referencesList) {
						Module module = reference.getModule();
						exprType expression = reference.getExpression();
						if (reference instanceof AttributeReference) {
							references.add(new MethodReference(method, expression, module));
						} else if (reference instanceof ClassReference) {
							references.add(new ConstructorReference(method, expression, module));
						}
					}
				}
			}
			
		} else {
			unexpectedSubgoal(subgoal);
		}
		
		return subgoals;
	}

	/**
	 * Sets up a {@link ResolveMethodGoal} for the given class 
	 */
	private void resolveMethod(Reference reference, ClassType classType, List<IGoal> subgoals) {
		String methodName = this.method.getName();
		ResolveMethodGoal rmg = new ResolveMethodGoal(this.context, classType, methodName);
		List<Reference> list = possibleReferences.get(rmg);
		if (list == null) {
			list = new LinkedList<Reference>();
			possibleReferences.put(rmg, list);
			subgoals.add(rmg);
		}
		list.add(reference);
	}
	
	/*
	 * Caching disabled for now, because the result depends on the InstanceContext.
	 */
	
//	/* casting is safe here */
//	@SuppressWarnings("unchecked")
//	@Override
//	public boolean checkCache() {
//		if (method.references != null) {
//			this.references.addAll((List<FunctionReference>) method.references);
//			return true;
//		} else {
//			return false;
//		}
//	}
//	
//	@Override
//	public void finish() {
//		method.references = this.references;
//	}
	

	private boolean isConstructor() {
		return method.getName().equals("__init__");
	}

}
