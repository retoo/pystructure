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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.BinOp;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.NameTok;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.jython.ast.expr_contextType;
import org.python.pydev.parser.jython.ast.name_contextType;
import org.python.pydev.parser.jython.ast.operatorType;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.GoalState;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;

/**
 * Evaluator for binary operations like +.
 */
public class BinOpTypeEvaluator extends AbstractEvaluator {

	private final BinOp binOp;
	
	private CombinedType result;
	
	private static final Map<Integer, String> METHODS = new HashMap<Integer, String>();
	static {
		// Method names from http://docs.python.org/ref/numeric-types.html
		METHODS.put(operatorType.Add, "__add__");
		METHODS.put(operatorType.Sub, "__sub__");
		METHODS.put(operatorType.Mult, "__mul__");
		METHODS.put(operatorType.FloorDiv, "__floordiv__");
		METHODS.put(operatorType.Mod, "__mod__");
		// __divmod__ is not an operator
		METHODS.put(operatorType.Pow, "__pow__");
		METHODS.put(operatorType.RShift, "__rshift__");
		METHODS.put(operatorType.BitAnd, "__and__");
		METHODS.put(operatorType.BitXor, "__xor__");
		METHODS.put(operatorType.BitOr, "__or__");
		
		// TODO: What about __radd__ and friends?
	}
	
	public BinOpTypeEvaluator(ExpressionTypeGoal goal, BinOp binOp) {
		super(goal);
		this.binOp = binOp;
		
		this.result = goal.resultType;
	}

	@Override
	public List<IGoal> init() {
		exprType receiver = binOp.left;
		String method = METHODS.get(binOp.op);
		if (method == null) {
			return IGoal.NO_GOALS;
		}
		
		NameTok methodTok = new NameTok(method, name_contextType.Attrib);
		Attribute func = new Attribute(receiver, methodTok, expr_contextType.Load);
		
		exprType argument = binOp.right;
		Call call = new Call(func, new exprType[] {argument}, null, null, null);
		
		return wrap(new ExpressionTypeGoal(getGoal().getContext(), call));
	}

	@Override
	public List<IGoal> subgoalDone(IGoal subgoal, GoalState state) {
		if (!(subgoal instanceof ExpressionTypeGoal)) { unexpectedSubgoal(subgoal); }
		
		ExpressionTypeGoal g = (ExpressionTypeGoal) subgoal;
		this.result.appendType(g.resultType);
		return IGoal.NO_GOALS;
	}

}
