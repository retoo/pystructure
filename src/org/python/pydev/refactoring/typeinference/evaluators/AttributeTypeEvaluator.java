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
import org.python.pydev.parser.jython.ast.Assign;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.refactoring.typeinference.dltk.goals.GoalState;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;
import org.python.pydev.refactoring.typeinference.goals.references.AttributeReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.types.ExpressionTypeGoal;
import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;
import org.python.pydev.refactoring.typeinference.model.definitions.Class;
import org.python.pydev.refactoring.typeinference.model.definitions.Definition;
import org.python.pydev.refactoring.typeinference.model.definitions.Method;
import org.python.pydev.refactoring.typeinference.model.definitions.Module;
import org.python.pydev.refactoring.typeinference.model.definitions.Package;
import org.python.pydev.refactoring.typeinference.model.definitions.PathElement;
import org.python.pydev.refactoring.typeinference.results.references.AttributeReference;
import org.python.pydev.refactoring.typeinference.results.types.ClassType;
import org.python.pydev.refactoring.typeinference.results.types.CombinedType;
import org.python.pydev.refactoring.typeinference.results.types.MetaclassType;
import org.python.pydev.refactoring.typeinference.results.types.MethodType;
import org.python.pydev.refactoring.typeinference.results.types.ModuleType;
import org.python.pydev.refactoring.typeinference.results.types.PackageType;

/**
 * Evaluator for the type of an attribute node. For example, the result of
 * <code>instance.method</code> would be the method.
 */
public class AttributeTypeEvaluator extends PythonEvaluator {

	private enum State { RECEIVER_WAIT, ASSIGNED_VALUE_WAIT, REFERENCES_WAIT };
	private State state;
	private final Attribute attribute;
	
	private CombinedType resultType;
	
	public AttributeTypeEvaluator(ExpressionTypeGoal goal, Attribute attribute) {
		super(goal);
		this.attribute = attribute;
		
		this.resultType = new CombinedType();
	}

	@Override
	public List<IGoal> init() {
		state = State.RECEIVER_WAIT;
		return wrap(new ExpressionTypeGoal(getGoal().getContext(), attribute.value));
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		List<IGoal> subgoals = new ArrayList<IGoal>();
		
		if (this.state == State.RECEIVER_WAIT) {
			
			NameAdapter attributeName = new NameAdapter(attribute.attr);
			
			for (IEvaluatedType type : EvaluatorUtils.extractTypes((IEvaluatedType) result)) {
				if (type instanceof ClassType) {
					// It's either a method or an attribute
					
					ClassType classType = (ClassType) type;
					Class klass = classType.getKlass();
					
					Method method = klass.getMethodNamed(attributeName);
					if (method != null) {
						resultType.appendType(new MethodType(classType.getModule(), method));
					} else {
						this.state = State.REFERENCES_WAIT;
						// It's probably a data attribute
						subgoals.add(new AttributeReferencesGoal(
								getGoal().getContext(),
								attributeName, klass));
					}
				} else if (type instanceof PackageType) {
					PackageType pkgType = (PackageType) type;
					
					Package pkg = pkgType.getPackage();
					
					PathElement child = pkg.getChild(attributeName);
					
					if (child != null) {
						
						if (child instanceof Package) {
							resultType.appendType(new PackageType((Package) child));
						} else if (child instanceof Module) {
							
							
							resultType.appendType(new ModuleType((Module) child));
						} else {
							throw new RuntimeException("Got SysPath, evil!");
						}
					} else {
						throw new RuntimeException("Unable to find " + attributeName + " in package " + pkg);
					}
				} else if (type instanceof ModuleType) {
					ModuleType moduleType = (ModuleType) type;
					
					Module module = moduleType.getModule();

					Definition child = module.getChild(attributeName);
					
					if (child instanceof Class) {
						resultType.appendType(new MetaclassType(module, (Class) child));
					} else {
						throw new RuntimeException("Unexpected child type");
					}
					
				}
				// TODO: PythonMetaclassType
			}
			
		} else if (this.state == State.REFERENCES_WAIT) {
			List<AttributeReference> references = (List<AttributeReference>) result;
			for (AttributeReference reference : references) {
				SimpleNode node = reference.getNode();
				if (node.parent instanceof Assign) {
					Assign assign = (Assign) node.parent;
					// TODO: Tuples
					if (assign.targets[0] == node) {
						this.state = State.ASSIGNED_VALUE_WAIT;
						subgoals.add(new ExpressionTypeGoal(
								getGoal().getContext(), assign.value));
					}
				}
			}
			
		} else if (this.state == State.ASSIGNED_VALUE_WAIT) {
			IEvaluatedType type = (IEvaluatedType) result;
			resultType.appendType(type);
		}
		
		return subgoals;
	}

	@Override
	public Object produceResult() {
		return resultType;
	}

}
