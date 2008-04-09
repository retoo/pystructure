/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
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
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ClassAttributeTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
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
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		if (state == GoalState.RECURSIVE) {
			// RECURSIVE could mean something like that:
			//   self.attr = self.attr
			// This doesn't add anything to the result type, so we ignore it.
			return IGoal.NO_GOALS;
		}
		
		List<IGoal> subgoals = new ArrayList<IGoal>();

		if (subgoal instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
			
			String attributeName = NodeUtils.getId(attribute.attr);
			for (IType type : g.resultType) {
				if (type instanceof ClassType) {
					// It's either a method or an attribute
					ClassType classType = (ClassType) type;
					Class klass = classType.getKlass();
	
					if (klass != null) {
						Method method = klass.getMethod(attributeName);
						if (method != null) {
							resultType.appendType(new MethodType(classType.getModule(), method));
						} else {
							ModuleContext context = new ModuleContext(getGoal().getContext(), klass.getModule());
							subgoals.add(new ClassAttributeTypeGoal(context, classType, attributeName));
						}
					} else {
						/* klass is null, this probably means that we are talking about 
						 * an internal or unknown class */
						
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
					ModuleContext context = new ModuleContext(getGoal().getContext(), module);
					subgoals.add(new DefinitionTypeGoal(context, child));
				}
				
				// TODO: PythonMetaclassType
			}
			
		} else if (subgoal instanceof ClassAttributeTypeGoal) {
			ClassAttributeTypeGoal g = (ClassAttributeTypeGoal) subgoal;
			resultType.appendType((CombinedType) g.resultType);
			
		} else if (subgoal instanceof DefinitionTypeGoal) {
			/* Was invoked by the ModuleType case above, might be used by other cases as well, later */
			DefinitionTypeGoal g = (DefinitionTypeGoal) subgoal;
			resultType.appendType(g.resultType);
			
		} else {
			unexpectedSubgoal(subgoal);
		}

		return subgoals;
	}

}
