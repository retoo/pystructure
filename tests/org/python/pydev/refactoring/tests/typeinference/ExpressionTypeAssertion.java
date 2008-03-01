/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.tests.typeinference;

import java.io.File;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Expr;
import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.dltk.inferencer.ITypeInferencer;
import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;
import org.python.pydev.refactoring.typeinference.goals.types.ExpressionTypeGoal;
import org.python.pydev.refactoring.typeinference.model.definitions.Module;
import org.python.pydev.refactoring.typeinference.visitors.Workspace;

class ExpressionTypeAssertion extends Assert {

	private final String correctClassRef;

	private final String filename;
	private final String expression;
	private final int line;

	public ExpressionTypeAssertion(String fileName, String expression,
			int line, String correctClassRef) {
		this.filename = fileName;
		this.expression = expression;
		assertNotNull(expression);
		this.line = line;
		this.correctClassRef = correctClassRef;
	}

	public void check(File file, ITypeInferencer inferencer, Workspace workspace) {
		Module module = workspace.getModule(file);
		SimpleNode rootNode = module.getNode();
		
		ExpressionAtPositionVisitor visitor = new ExpressionAtPositionVisitor(expression, line);
		
		try {
			visitor.traverse(rootNode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		Expr expression = visitor.getExpression();

		if (expression == null) {
			throw new RuntimeException("Unable to find node for expresssion '" + this.expression + "'");
		}
		
		PythonContext context = new PythonContext(workspace, module);
		ExpressionTypeGoal goal = new ExpressionTypeGoal(context, expression.value);
		IEvaluatedType type = inferencer.evaluateType(goal, -1);
		if (!correctClassRef.equals("recursion")) {
			if (type == null) {
				throw new AssertionFailedError(
						"null type fetched, but "
								+ correctClassRef + " expected");
			}
			assertNotNull(type);
			assertType(correctClassRef, type.getTypeName());
		}
	}
	
	private void assertType(final String expected, final String actual) {
		if (!expected.equals(actual)) {
			throw new AssertionFailedError() {
				private static final long serialVersionUID = 1L;

				public String toString() {
					String s 
						= "Type of <" + expression + ">: expected <" + expected 
						+ "> but was <" + actual 
						+ "> (" + filename + ":" + line + ")";
					return s;
				}
			};
		}
	}

}

