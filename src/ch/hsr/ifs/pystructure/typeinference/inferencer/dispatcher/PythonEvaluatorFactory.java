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

package ch.hsr.ifs.pystructure.typeinference.inferencer.dispatcher;

import java.util.HashMap;
import java.util.Map;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.BinOp;
import org.python.pydev.parser.jython.ast.BoolOp;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.Compare;
import org.python.pydev.parser.jython.ast.Dict;
import org.python.pydev.parser.jython.ast.IfExp;
import org.python.pydev.parser.jython.ast.Lambda;
import org.python.pydev.parser.jython.ast.List;
import org.python.pydev.parser.jython.ast.ListComp;
import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.parser.jython.ast.Num;
import org.python.pydev.parser.jython.ast.Str;
import org.python.pydev.parser.jython.ast.StrJoin;
import org.python.pydev.parser.jython.ast.Subscript;
import org.python.pydev.parser.jython.ast.Tuple;
import org.python.pydev.parser.jython.ast.UnaryOp;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.jython.ast.num_typeType;

import ch.hsr.ifs.pystructure.typeinference.evaluators.base.AbstractEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.AttributeReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.ClassReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.FunctionReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.MethodReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.PossibleAttributeReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.PossibleReferencesEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.references.VariableReferenceEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.FixedResultEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.ArgumentTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.AssignTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.AttributeTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.BinOpTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.CallTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.ClassAttributeTypeEvaluator;
import ch.hsr.ifs.pystructure.typeinference.evaluators.types.IfExpTypeEvaluator;
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
import ch.hsr.ifs.pystructure.typeinference.goals.types.AbstractTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ClassAttributeTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.DefinitionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ReturnTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.goals.types.TupleElementTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.AssignDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ExceptDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.LoopVariableDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NoDefintion;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.FunctionType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.ModuleType;
import ch.hsr.ifs.pystructure.typeinference.results.types.TupleType;

/**
 * Evaluator factory which, given a goal, creates the appropriate evaluator. It
 * can be seen as a kind of dispatcher.
 */
public class PythonEvaluatorFactory {
	
	private final Map<Class<? extends IGoal>, Class<? extends AbstractEvaluator>> evaluators;
	
	public PythonEvaluatorFactory() {
		evaluators = new HashMap<Class<? extends IGoal>, Class<? extends AbstractEvaluator>>();
		initEvaluatorMap();
	}

	private void initEvaluatorMap() {
		evaluators.put(PossibleReferencesGoal.class, PossibleReferencesEvaluator.class);
		evaluators.put(PossibleAttributeReferencesGoal.class, PossibleAttributeReferencesEvaluator.class);
		evaluators.put(MethodReferencesGoal.class, MethodReferencesEvaluator.class);
		evaluators.put(FunctionReferencesGoal.class, FunctionReferencesEvaluator.class);
		evaluators.put(AttributeReferencesGoal.class, AttributeReferencesEvaluator.class);
		evaluators.put(ClassReferencesGoal.class, ClassReferencesEvaluator.class);
		evaluators.put(ReturnTypeGoal.class, ReturnTypeEvaluator.class);
		evaluators.put(TupleElementTypeGoal.class, TupleElementTypeEvaluator.class);
		evaluators.put(ClassAttributeTypeGoal.class, ClassAttributeTypeEvaluator.class);
		evaluators.put(PossibleReferencesGoal.class, PossibleReferencesEvaluator.class);
		evaluators.put(PossibleReferencesGoal.class, PossibleReferencesEvaluator.class);
	}

	public AbstractEvaluator createEvaluator(IGoal goal) {
		AbstractEvaluator evaluator = createEvaluatorFromMap(goal);
		if (evaluator != null) {
			return evaluator;
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
	
	private AbstractEvaluator createEvaluatorFromMap(IGoal goal) {
		Class<? extends IGoal> goalClass = goal.getClass();
		Class<? extends AbstractEvaluator> evaluatorClass = evaluators.get(goalClass);
		if (evaluatorClass == null) {
			return null;
		}
		
		try {
			AbstractEvaluator evaluator = evaluatorClass.getConstructor(goalClass).newInstance(goal);
			return evaluator;
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	private AbstractEvaluator createDefinitionEvaluator(DefinitionTypeGoal goal) {
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
			return new FixedResultEvaluator(goal, new FunctionType(module, function));
		}
		if (def instanceof ch.hsr.ifs.pystructure.typeinference.model.definitions.Class) {
			ch.hsr.ifs.pystructure.typeinference.model.definitions.Class klass = (ch.hsr.ifs.pystructure.typeinference.model.definitions.Class) def;
			return new FixedResultEvaluator(goal, new MetaclassType(module, klass));
		}
		if (def instanceof Module) {
			Module moduleDef = (Module) def;
			return new FixedResultEvaluator(goal, new ModuleType(moduleDef));
		}
		if (def instanceof LoopVariableDefinition) {
			// TODO: Implement LoopVariableTypeEvaluator
			return new FixedResultEvaluator(goal, new ClassType("object"));
		}
		if (def instanceof ExceptDefinition) {
			// TODO: Implement ExceptTypeEvaluator
			return new FixedResultEvaluator(goal, new ClassType("object"));
		}
		if (def instanceof NoDefintion) {
			return new FixedResultEvaluator(goal, new ClassType("object"));
		}
		
		throw new RuntimeException("Can't create evaluator for definition " + def + ", goal " + goal);
	}
	
	private AbstractEvaluator createExpressionEvaluator(ExpressionTypeGoal goal) {
		SimpleNode expr = goal.getExpression();

		if (expr instanceof Name) {
			Name name = (Name) expr;
			if (name.id.equals("None")) {
				/* FIXME: shoudln't we create a new class for NoneType? */
				return new FixedResultEvaluator(goal, new ClassType("NoneType"));
			}
			if (name.id.equals("True") || name.id.equals("False")) {
				return new FixedResultEvaluator(goal, new ClassType("bool"));
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
		if (expr instanceof IfExp) {
			return new IfExpTypeEvaluator(goal, (IfExp) expr);
		}
		
		return createLiteralEvaluator(goal);
	}
	
	// TODO: Maybe move this into an ExpressionTypeEvaluator.
	private AbstractEvaluator createLiteralEvaluator(ExpressionTypeGoal goal) {
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
			return new FixedResultEvaluator(goal, new ClassType(type));
		}
		if (expr instanceof Str) {
			Str str = (Str) expr;
			return createStrEvaluator(goal, str.unicode);
		}
		if (expr instanceof List) {
			return new FixedResultEvaluator(goal, new ClassType("list"));
		}
		if (expr instanceof Tuple) {
			return new FixedResultEvaluator(goal, new TupleType((Tuple) expr));
		}
		if (expr instanceof Dict) {
			return new FixedResultEvaluator(goal, new ClassType("dict"));
		}
		if (expr instanceof Subscript) {
			return new FixedResultEvaluator(goal, new ClassType("list-element"));
		}
		if (expr instanceof ListComp) {
			/* FIXME: this is a expression like:
			 *  [field for field in self.fields if field.isempty()
			 *  
			 *  we could use the generators.iter to find out what kind of 
			 *  list we have to expect here, but for now we have no idea about lists anyway
			 */
			return new FixedResultEvaluator(goal, new ClassType("list"));
		}
		if (expr instanceof Compare) {
			return new FixedResultEvaluator(goal, new ClassType("bool"));
		}
		if (expr instanceof UnaryOp) {
			return new FixedResultEvaluator(goal, new ClassType("bool"));
		}
		if (expr instanceof BoolOp) {
			return new FixedResultEvaluator(goal, new ClassType("bool"));
		}
		if (expr instanceof Lambda) {
			// FIXME: Implement properly (like function)
			return new FixedResultEvaluator(goal, new ClassType("function"));
		}
		if (expr instanceof StrJoin) {
			StrJoin strJoin = (StrJoin) expr;
			boolean isUnicode = false;
			for (exprType str : strJoin.strs) {
				if (((Str) str).unicode) {
					isUnicode = true;
				}
			}
			return createStrEvaluator(goal, isUnicode);
		}
		if (expr instanceof StrJoin) {
			return new FixedResultEvaluator(goal, new ClassType("str"));			
		}
		
		throw new RuntimeException("Can't create evaluator for literal expression " + expr +  ", goal " + goal);
	}

	private AbstractEvaluator createStrEvaluator(AbstractTypeGoal goal, boolean isUnicode) {
		if (isUnicode) {
			return new FixedResultEvaluator(goal, new ClassType("unicode"));
		} else {
			return new FixedResultEvaluator(goal, new ClassType("str"));
		}
	}

}
