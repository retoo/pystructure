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

import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.Index;
import org.python.pydev.parser.jython.ast.NameTok;
import org.python.pydev.parser.jython.ast.Num;
import org.python.pydev.parser.jython.ast.Subscript;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.jython.ast.expr_contextType;
import org.python.pydev.parser.jython.ast.name_contextType;
import org.python.pydev.parser.jython.ast.num_typeType;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;

/**
 * Evaluator for the subscript operator. There are two cases here:
 * 
 * letters = ['a', 'b', 'c']
 * letters[0]    # Case 1, returns 'a'
 * letters[0:2]  # Case 2, returns ['a', 'b']
 * 
 * The first case calls __getitem__ on the object and the second calls
 * __getslice__. Note: __getslice__ is deprecated since Python 2.0, but we need
 * it to distinguish the two cases (one returns a single element, the other a
 * list).
 */
public class SubscriptTypeEvaluator extends AbstractEvaluator {

	private final Subscript subscript;
	private CombinedType resultType;

	public SubscriptTypeEvaluator(ExpressionTypeGoal goal, Subscript subscript) {
		super(goal);
		this.subscript = subscript;
		
		this.resultType = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		/*
		 * We create the node for the corresponding method call and then use an
		 * ExpressionTypeGoal to get the resulting type:
		 * 
		 * letters[0]     →  letters.__getitem__(0)
		 * letters[2:3]   →  letters.__getslice__(0, 0)
		 * letters[0::2]  →  letters.__getslice__(0, 0)
		 * 
		 * Note that dummy values are used for the arguments to __getslice__,
		 * because the real values don't map directly to these two arguments.
		 */
		
		String method;
		exprType[] arguments;
		
		if (subscript.slice instanceof Index) {
			/* __getitem__ case */
			method = "__getitem__";
			Index index = (Index) subscript.slice;
			arguments = new exprType[] {index.value};
			
		} else {
			/* __getslice__ case */
			method = "__getslice__";
			
			/*
			 * We can't use the real argument types, because they are different
			 * for the different slice objects. So we take two dummy values.
			 */
			Num num1 = new Num(Integer.valueOf(0), num_typeType.Int, "0");
			Num num2 = new Num(Integer.valueOf(0), num_typeType.Int, "0");
			arguments = new exprType[] {num1, num2};
		}
		
		exprType receiver = subscript.value;
		NameTok methodTok = new NameTok(method, name_contextType.Attrib);
		Attribute func = new Attribute(receiver, methodTok, expr_contextType.Load);
		
		Call call = new Call(func, arguments, null, null, null);

		return wrap(new ExpressionTypeGoal(getGoal().getContext(), call));
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		if (!(subgoal instanceof ExpressionTypeGoal)) { unexpectedSubgoal(subgoal); }
		
		ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
		resultType.appendType(g.resultType);
		return IGoal.NO_GOALS;
	}

}
