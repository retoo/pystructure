/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
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

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Assign;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.AttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ClassAttributeTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Attribute;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.AbstractType;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;

public class ClassAttributeTypeEvaluator extends AbstractEvaluator {

	private ClassType classType;
	private String attributeName;
	private CombinedType resultType;
	private Class klass;
	private Attribute attribute;

	public ClassAttributeTypeEvaluator(ClassAttributeTypeGoal goal) {
		super(goal);

		this.classType = goal.getClassType();
		this.klass = classType.getKlass();
		this.attributeName = goal.getAttributeName();
		
		/* fetch the attribute (and create one if there isn't already one) */
		attribute = klass.getAttribute(attributeName);
		
		if (attribute == null) {
			attribute = new Attribute(attributeName, klass);
			klass.addAttribute(attribute);
		}
		
		this.resultType = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		return wrap(new AttributeReferencesGoal(getGoal().getContext(), attribute, classType));
	}
	
	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		ArrayList<IGoal> subgoals = new ArrayList<IGoal>();
		
		if (subgoal instanceof AttributeReferencesGoal) {
			AttributeReferencesGoal g = (AttributeReferencesGoal) subgoal;
			
			for (AttributeReference reference : g.references) {
				SimpleNode node = reference.getExpression();
				if (node.parent instanceof Assign) {
					Assign assign = (Assign) node.parent;
					// TODO: Tuples
					if (assign.targets[0] == node) {
						// The ExpressionTypeGoal has to be evaluated in the
						// context of the reference (its module)
						ModuleContext context = new ModuleContext(getGoal().getContext(), reference.getModule());
						subgoals.add(new ExpressionTypeGoal(context, assign.value));
					} else {
						/* skip .. ? */	
					}
				} /* skip all non-assign nodes */
			}
			
		} else if (subgoal instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
			resultType.appendType(g.resultType);
			
		} else {
			unexpectedSubgoal(subgoal);
		}

		return subgoals;
	}
	
	@Override
	public void finish() {
		for (IType type : resultType) {
			if (type instanceof AbstractType) {
				((AbstractType) type).location = attribute;
			}
		}
		if (attribute.type == null) {
			attribute.type = new CombinedType();
		}
		attribute.type.appendType(resultType);
	}

}
