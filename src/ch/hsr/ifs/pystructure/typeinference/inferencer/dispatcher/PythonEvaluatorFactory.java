/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher;

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

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.GoalEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.AttributeReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.ClassReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.FunctionReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.MethodReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.PossibleAttributeReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.PossibleReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.VariableReferenceEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.ArgumentTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.AssignTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.AttributeTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.BinOpTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.CallTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.ClassAttributeTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.FixedAnswerEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.ImportTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.ReturnTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.TupleElementTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.goals.base.IGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.AttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.ClassReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.FunctionReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.MethodReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleAttributeReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.references.PossibleReferencesGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ClassAttributeTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ReturnTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.TupleElementTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AssignDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ExceptDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.LoopVariableDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.FunctionType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.ModuleType;
import ch.hsr.ifs.pystructure.typeinference.results.types.TupleType;

/**
 * Evaluator factory which, given a goal, creates the appropriate evaluator. It
 * can be seen as a kind of dispatcher.
 */
public class PythonEvaluatorFactory implements IEvaluatorFactory {

	public GoalEvaluator createEvaluator(IGoal goal) {
		if (goal instanceof PossibleReferencesGoal) {
			return new PossibleReferencesEvaluator((PossibleReferencesGoal) goal);
		}
		if (goal instanceof PossibleAttributeReferencesGoal) {
			return new PossibleAttributeReferencesEvaluator((PossibleAttributeReferencesGoal) goal);
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
		if (goal instanceof ClassAttributeTypeGoal) {
			return new ClassAttributeTypeEvaluator((ClassAttributeTypeGoal) goal);
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
		if (def instanceof ImportDefinition) {
			return new ImportTypeEvaluator(goal, (ImportDefinition) def);
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
		if (def instanceof LoopVariableDefinition) {
			// TODO: Implement LoopVariableTypeEvaluator
			return new FixedAnswerEvaluator(goal, new ClassType("object"));
		}
		if (def instanceof ExceptDefinition) {
			// TODO: Implement ExceptTypeEvaluator
			return new FixedAnswerEvaluator(goal, new ClassType("object"));
		}
		
		throw new RuntimeException("Can't create evaluator for definition " + def + ", goal " + goal);
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
		
		throw new RuntimeException("Can't create Evaluator for " + goal);
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
		
		throw new RuntimeException("Can't create evaluator for literal expression " + expr +  ", goal " + goal);
	}

}
