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

import java.util.LinkedList;
import java.util.List;

import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.Index;
import org.python.pydev.parser.jython.ast.Num;
import org.python.pydev.parser.jython.ast.Subscript;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.jython.ast.num_typeType;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.results.types.TupleType;

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
 * 
 * There's also the special case of a tuple element access directly by number
 * (not indirectly with index variable):
 * 
 * tuple = (42, 3.14)
 * tuple[1] ## type float
 */
public class SubscriptTypeEvaluator extends AbstractEvaluator {

	private final Subscript subscript;
	private CombinedType resultType;

	private enum State { WAITING_RECEIVER, WAITING_RESULT };
	private State state;

	public SubscriptTypeEvaluator(ExpressionTypeGoal goal, Subscript subscript) {
		super(goal);
		this.subscript = subscript;
		
		this.resultType = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		this.state = State.WAITING_RECEIVER;
		return wrap(new ExpressionTypeGoal(getGoal().getContext(), subscript.value));
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState subgoalState) {
		if (!(subgoal instanceof ExpressionTypeGoal)) { unexpectedSubgoal(subgoal); }
		
		ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
		
		if (this.state == State.WAITING_RECEIVER) {
			this.state = State.WAITING_RESULT;
			
			/* Check if this is a direct tuple element access */
			Integer directIndex = null;
			
			if (subscript.slice instanceof Index) {
				Index indexSlice = (Index) subscript.slice;
				/* May also be set to null if the index is no integer */
				directIndex = NodeUtils.getInteger(indexSlice.value);
			}
			
			List<IGoal> subgoals = new LinkedList<IGoal>();
			
			boolean shouldAddMethodCallGoal = false;
			
			for (IType type : g.resultType) {
				if (directIndex != null && type instanceof TupleType) {
					/* This is a direct tuple element access */
					IGoal tupleElementGoal = createTupleElementGoal((TupleType) type, directIndex);
					if (tupleElementGoal != null) {
						subgoals.add(tupleElementGoal);
					}
				} else {
					/* No direct tuple element access */
					shouldAddMethodCallGoal = true;
				}
			}
			
			if (shouldAddMethodCallGoal) {
				subgoals.add(createMethodCallGoal());
			}
			
			return subgoals;
		
		} else {
			resultType.appendType(g.resultType);
			return IGoal.NO_GOALS;
		}
	}
	
	/**
	 * For a direct tuple element access, we create an ExpressionTypeGoal for
	 * the element:
	 * 
	 * (42, 3.14)[0]  # goal for expression 42 is created
	 * 
	 * If the index is out of range, null is returned.
	 */
	private ExpressionTypeGoal createTupleElementGoal(TupleType tupleType, int index) {
		exprType[] elements = tupleType.getTuple().elts;
		
		int actualIndex = (index < 0) ? (elements.length + index) : index;
		
		if (actualIndex < 0 || actualIndex >= elements.length) {
			/*
			 * The tuple element index is out of range. This only happens in
			 * complex situations and then we ignore this tuple element.
			 * Example:
			 * 
			 * a, b, c = 1, 2
			 */
			return null;
		}
		
		exprType expression = elements[actualIndex];
		
		return new ExpressionTypeGoal(getGoal().getContext(), expression);
	}

	/**
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
	private ExpressionTypeGoal createMethodCallGoal() {
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
		Call call = NodeUtils.createMethodCall(receiver, method, arguments);
		
		return new ExpressionTypeGoal(getGoal().getContext(), call);
	}

}
