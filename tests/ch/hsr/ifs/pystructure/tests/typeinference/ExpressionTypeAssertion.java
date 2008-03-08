/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.tests.typeinference;

import java.io.File;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Expr;

import ch.hsr.ifs.pystructure.typeinference.contexts.PythonContext;
import ch.hsr.ifs.pystructure.typeinference.dltk.inferencer.ITypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.dltk.types.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.goals.types.ExpressionTypeGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.ExpressionAtLineVisitor;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

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
		
		ExpressionAtLineVisitor visitor = new ExpressionAtLineVisitor(line);
		
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
