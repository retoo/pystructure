/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.evaluators;

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
import org.python.pydev.refactoring.typeinference.dltk.goals.GoalState;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.goals.types.ExpressionTypeGoal;

/**
 * Evaluator for binary operations like +.
 */
public class BinOpTypeEvaluator extends PythonEvaluator {

	private final BinOp binOp;
	
	private Object result;
	
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
	public List<IGoal> subGoalDone(IGoal subgoal, Object result, GoalState state) {
		this.result = result;
		return IGoal.NO_GOALS;
	}

	@Override
	public Object produceResult() {
		return result;
	}

}
