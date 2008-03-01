/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.python.pydev.refactoring.typeinference.evaluators;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.BinOp;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.Dict;
import org.python.pydev.parser.jython.ast.List;
import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.parser.jython.ast.Num;
import org.python.pydev.parser.jython.ast.Str;
import org.python.pydev.parser.jython.ast.Tuple;
import org.python.pydev.parser.jython.ast.num_typeType;
import org.python.pydev.refactoring.typeinference.dltk.evaluators.FixedAnswerEvaluator;
import org.python.pydev.refactoring.typeinference.dltk.evaluators.GoalEvaluator;
import org.python.pydev.refactoring.typeinference.dltk.evaluators.IGoalEvaluatorFactory;
import org.python.pydev.refactoring.typeinference.dltk.goals.IGoal;
import org.python.pydev.refactoring.typeinference.goals.references.AttributeReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.references.ClassReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.references.FunctionReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.references.MethodReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.references.PossibleAttributeReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.references.PossibleReferencesGoal;
import org.python.pydev.refactoring.typeinference.goals.types.DefinitionTypeGoal;
import org.python.pydev.refactoring.typeinference.goals.types.ExpressionTypeGoal;
import org.python.pydev.refactoring.typeinference.goals.types.ReturnTypeGoal;
import org.python.pydev.refactoring.typeinference.goals.types.TupleElementTypeGoal;
import org.python.pydev.refactoring.typeinference.model.definitions.Argument;
import org.python.pydev.refactoring.typeinference.model.definitions.AssignDefinition;
import org.python.pydev.refactoring.typeinference.model.definitions.Class;
import org.python.pydev.refactoring.typeinference.model.definitions.Definition;
import org.python.pydev.refactoring.typeinference.model.definitions.ExceptDefinition;
import org.python.pydev.refactoring.typeinference.model.definitions.Function;
import org.python.pydev.refactoring.typeinference.model.definitions.ImportDefinition;
import org.python.pydev.refactoring.typeinference.model.definitions.LoopVariableDefinition;
import org.python.pydev.refactoring.typeinference.model.definitions.Module;
import org.python.pydev.refactoring.typeinference.results.types.ClassType;
import org.python.pydev.refactoring.typeinference.results.types.FunctionType;
import org.python.pydev.refactoring.typeinference.results.types.MetaclassType;
import org.python.pydev.refactoring.typeinference.results.types.ModuleType;
import org.python.pydev.refactoring.typeinference.results.types.TupleType;

/**
 * Default evaluator factory used by {@link PythonEvaluatorFactory}.
 */
public class DefaultPythonEvaluatorFactory implements IGoalEvaluatorFactory {

	public GoalEvaluator createEvaluator(IGoal goal) {
		if (goal instanceof PossibleReferencesGoal) {
			if (goal instanceof PossibleAttributeReferencesGoal) {
				return new PossibleAttributeReferencesEvaluator((PossibleAttributeReferencesGoal) goal);
			} else {
				return new PossibleReferencesEvaluator((PossibleReferencesGoal) goal);
			}
		}
		if (goal instanceof MethodReferencesGoal) {
			return new MethodReferencesEvaluator((MethodReferencesGoal) goal);
		}
		if (goal instanceof FunctionReferencesGoal) {
			return new FunctionReferencesEvaluator((FunctionReferencesGoal) goal);
		}
		if (goal instanceof AttributeReferencesGoal) {
			return new AttributeReferencesEvaluator((AttributeReferencesGoal) goal);
		}
		if (goal instanceof ClassReferencesGoal) {
			return new ClassReferencesEvaluator((ClassReferencesGoal) goal);
		}
		if (goal instanceof ReturnTypeGoal) {
			return new ReturnTypeEvaluator((ReturnTypeGoal) goal);
		}
		if (goal instanceof TupleElementTypeGoal) {
			return new TupleElementTypeEvaluator((TupleElementTypeGoal) goal);
		}
		
		if (goal instanceof DefinitionTypeGoal) {
			DefinitionTypeGoal defGoal = (DefinitionTypeGoal) goal;
			return createDefinitionEvaluator(defGoal);
		}
		
		if (goal instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal exprGoal = (ExpressionTypeGoal) goal;
			return createExpressionEvaluator(exprGoal);
		}
		
		throw new RuntimeException("Can't create Evaluator for " + goal);
	}
	
	private GoalEvaluator createDefinitionEvaluator(DefinitionTypeGoal goal) {
		Definition def = goal.getDefinition();
		Module module = goal.getContext().getModule();
		
		if (def instanceof AssignDefinition) {
			return new AssignTypeEvaluator(goal, (AssignDefinition) def);
		}
		if (def instanceof Argument) {
			return new ArgumentTypeEvaluator(goal, (Argument) def);
		}
		if (def instanceof Function) {
			Function function = (Function) def;
			return new FixedAnswerEvaluator(goal, new FunctionType(module, function));
		}
		if (def instanceof Class) {
			Class klass = (Class) def;
			return new FixedAnswerEvaluator(goal, new MetaclassType(module, klass));
		}
		if (def instanceof Module) {
			Module moduleDef = (Module) def;
			return new FixedAnswerEvaluator(goal, new ModuleType(moduleDef));
		}
		if (def instanceof ImportDefinition) {
			return new ImportTypeEvaluator(goal, (ImportDefinition) def);
		}
		if (def instanceof LoopVariableDefinition) {
			// TODO: Implement LoopVariableTypeEvaluator
			return new FixedAnswerEvaluator(goal, new ClassType("object"));
		}
		if (def instanceof ExceptDefinition) {
			// TODO: Implement ExceptTypeEvaluator
			return new FixedAnswerEvaluator(goal, new ClassType("object"));
		}
		
		return null;
	}
	
	private GoalEvaluator createExpressionEvaluator(ExpressionTypeGoal goal) {
		SimpleNode expr = goal.getExpression();

		if (expr instanceof Name) {
			Name name = (Name) expr;
			if (name.id.equals("None")) {
				return new FixedAnswerEvaluator(goal, new ClassType("NoneType"));
			}
			if (name.id.equals("True") || name.id.equals("False")) {
				return new FixedAnswerEvaluator(goal, new ClassType("bool"));
			}
			return new VariableReferenceEvaluator(goal, name);
		}
		if (expr instanceof Call) {
			return new CallTypeEvaluator(goal, (Call) expr);
		}
		if (expr instanceof Attribute) {
			return new AttributeTypeEvaluator(goal, (Attribute) expr);
		}
		if (expr instanceof BinOp) {
			return new BinOpTypeEvaluator(goal, (BinOp) expr);
		}
		
		GoalEvaluator evaluator = createLiteralEvaluator(goal);
		if (evaluator != null) {
			return evaluator;
		}
		
		return null;
	}
	
	// TODO: Maybe move this into an ExpressionTypeEvaluator.
	private GoalEvaluator createLiteralEvaluator(ExpressionTypeGoal goal) {
		SimpleNode expr = goal.getExpression();
		
		if (expr instanceof Num) {
			Num num = (Num) expr;
			String type = null;
			switch (num.type) {
			case num_typeType.Long: type = "long"; break;
			case num_typeType.Float: type = "float"; break;
			case num_typeType.Comp: type = "complex"; break;
			case num_typeType.Int:
			case num_typeType.Oct:
			case num_typeType.Hex:
			default: type = "int"; break;
			}
			return new FixedAnswerEvaluator(goal, new ClassType(type));
		}
		if (expr instanceof Str) {
			Str str = (Str) expr;
			if (str.unicode) {
				return new FixedAnswerEvaluator(goal, new ClassType("unicode"));
			} else {
				return new FixedAnswerEvaluator(goal, new ClassType("str"));
			}
		}
		if (expr instanceof List) {
			return new FixedAnswerEvaluator(goal, new ClassType("list"));
		}
		if (expr instanceof Tuple) {
			return new FixedAnswerEvaluator(goal, new TupleType((Tuple) expr));
		}
		if (expr instanceof Dict) {
			return new FixedAnswerEvaluator(goal, new ClassType("dict"));
		}
		return null;
	}

}
