/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators.types;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Attribute;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ClassAttributeTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MethodType;
import ch.hsr.ifs.pystructure.typeinference.results.types.ModuleType;
import ch.hsr.ifs.pystructure.typeinference.results.types.PackageType;

/**
 * Evaluator for the type of an attribute node. For example, the result of
 * <code>instance.method</code> would be the method.
 */
public class AttributeTypeEvaluator extends AbstractEvaluator {

	private final Attribute attribute;

	private CombinedType resultType;

	public AttributeTypeEvaluator(ExpressionTypeGoal goal, Attribute attribute) {
		super(goal);
		this.attribute = attribute;

		this.resultType = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		return wrap(new ExpressionTypeGoal(getGoal().getContext(), attribute.value));
	}

	@Override
	public List<IGoal> subGoalDone(IGoal subgoal, GoalState state) {
		if (state == GoalState.RECURSIVE) {
			// RECURSIVE could mean something like that:
			//   self.attr = self.attr
			// This doesn't add anything to the result type, so we ignore it.
			return IGoal.NO_GOALS;
		}
		
		List<IGoal> subgoals = new ArrayList<IGoal>();

		if (subgoal instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
			
			NameAdapter attributeName = new NameAdapter(attribute.attr);
			for (IType type : g.resultType) {
				if (type instanceof ClassType) {
					// It's either a method or an attribute
					ClassType classType = (ClassType) type;
					Class klass = classType.getKlass();
	
					Method method = klass.getMethodNamed(attributeName);
					if (method != null) {
						resultType.appendType(new MethodType(classType.getModule(), method));
					} else {
						subgoals.add(new ClassAttributeTypeGoal(getGoal().getContext(), classType, attributeName));
	
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
			
		} else if (subgoal instanceof ClassAttributeTypeGoal) {
			ClassAttributeTypeGoal g = (ClassAttributeTypeGoal) subgoal;
			resultType.appendType((CombinedType) g.resultType);
			
		} else {
			throw new RuntimeException("Unknown subgoal");
		}

		return subgoals;
	}

}
