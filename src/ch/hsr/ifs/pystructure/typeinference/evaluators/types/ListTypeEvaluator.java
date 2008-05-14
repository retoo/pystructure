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

import java.util.List;

import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

/**
 * Evaluator for evaluating the type of list literals:
 * 
 * l = [42, 3.14]
 * l[0] ## type float|int
 * 
 * The special thing here is that the elements (42, 3.14) in the literal
 * constructor need to be made available so they will be found.
 */
public class ListTypeEvaluator extends AbstractEvaluator {

	private final org.python.pydev.parser.jython.ast.List list;
	private CombinedType resultType;

	public ListTypeEvaluator(ExpressionTypeGoal goal, org.python.pydev.parser.jython.ast.List list) {
		super(goal);
		this.list = list;
		
		this.resultType = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		String className = "list";
		Workspace workspace = getGoal().getContext().getWorkspace();
		
		List<Definition> definitions = workspace.getBuiltinModule().getDefinitions(className);
		if (definitions.size() != 1 || !(definitions.get(0) instanceof Class)) {
			throw new RuntimeException("Built-in definition of " + className + " is invalid");
		}
		Class klass = (Class) definitions.get(0);
		
		Call constructorCall = NodeUtils.createFunctionCall(className);
		ClassType classType = new ClassType(klass, constructorCall);
		resultType.appendType(classType);
		
		for (exprType element : list.elts) {
			Call call = NodeUtils.createMethodCall(list, "append", element);
			AttributeReference attributeReference = new AttributeReference("append", resultType, call.func, getGoal().getContext().getModule());
			workspace.addPossibleAttributeReference(attributeReference);
		}
		
		return IGoal.NO_GOALS;
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		return IGoal.NO_GOALS;
	}

}
