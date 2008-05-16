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
import ch.hsr.ifs.pystructure.typeinference.goals.misc.MethodResolveGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ClassAttributeTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.ModuleType;
import ch.hsr.ifs.pystructure.typeinference.results.types.PackageType;

/**
 * Evaluator for the type of an attribute node. For example, the result for
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
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		if (subgoalState == GoalState.RECURSIVE) {
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
						ModuleContext context = new ModuleContext(getGoal().getContext(), klass.getModule());
						subgoals.add(new MethodResolveGoal(context, classType, attributeName));
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
						System.err.println("Unable to find " + attributeName + " in package " + pkg);
//						throw new RuntimeException("Unable to find " + attributeName + " in package " + pkg);
					}
					
				} else if (type instanceof ModuleType) {
					ModuleType moduleType = (ModuleType) type;
					Module module = moduleType.getModule();
	
					List<Definition> definitions = module.getDefinitions(attributeName);
					for (Definition definition : definitions) {
						ModuleContext context = new ModuleContext(getGoal().getContext(), module);
						subgoals.add(new DefinitionTypeGoal(context, definition));
					}
				}
				
				/*
				 * TODO: What if the receiver is MetaclassType?:
				 * 
				 * class B(object):
				 *     def method(self):
				 *         return 42
				 * 
				 * b = B()
				 * B.method(b) ## type int
				 */
			}
			
		} else if (subgoal instanceof ClassAttributeTypeGoal) {
			ClassAttributeTypeGoal g = (ClassAttributeTypeGoal) subgoal;
			resultType.appendType(g.resultType);
		
		} else if (subgoal instanceof MethodResolveGoal) {
			MethodResolveGoal g = (MethodResolveGoal) subgoal;
			
			if (!g.methodTypes.isEmpty()) {
				resultType.appendType(g.methodTypes);
			} else {
				/* If we can't find a method with this name (both in the actual class
				 * and in the hierarchy we are trying to look for a class attribute 
				 */
				ClassType classType = g.getClassType();
				String attributeName = g.getAttributeName();
				ModuleContext context = new ModuleContext(getGoal().getContext(), classType.getKlass().getModule());
				subgoals.add(new ClassAttributeTypeGoal(context, classType, attributeName));
			}	
			
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
