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

package ch.hsr.ifs.pystructure.tests.typeinference;

import java.io.File;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Expr;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.ExpressionAtLineVisitor;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

class ExpressionTypeAssertion extends Assert {

	private final String correctClassRef;

	public final String filename;
	public final String expression;
	public final int line;

	public ExpressionTypeAssertion(String fileName, String expression,
			int line, String correctClassRef) {
		this.filename = fileName;
		this.expression = expression;
		assertNotNull(expression);
		this.line = line;
		this.correctClassRef = correctClassRef;
	}

	public void check(File file, PythonTypeInferencer inferencer, Workspace workspace) {
		Module module = workspace.getModule(file);
		
		ExpressionAtLineVisitor visitor = new ExpressionAtLineVisitor(line);
		Expr expression = visitor.run(module.getNode());
		
		if (expression == null) {
			throw new RuntimeException("Unable to find node for expresssion '" + this.expression + "'");
		}
		
		IType type = inferencer.evaluateType(workspace, module, expression.value);
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

