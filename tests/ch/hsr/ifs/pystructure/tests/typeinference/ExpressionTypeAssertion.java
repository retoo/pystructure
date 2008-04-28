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


import junit.framework.AssertionFailedError;

import org.python.pydev.parser.jython.ast.Expr;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

class ExpressionTypeAssertion extends InferencerAssertion {

	protected final String correctClassRef;

	public ExpressionTypeAssertion(String fileName, String expression,
			int line, String correctClassRef) {
		super(fileName, expression, line);
		
		this.correctClassRef = correctClassRef;
	}

	public void doIt(PythonTypeInferencer inferencer, Workspace workspace, Module module, Expr expression) {
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
	
	private void assertType(String expected, String actual) {
		if (!expected.equals(actual)) {
			throw new InferencerAssertionError("type", expected, actual, expected, filename, line); 
		}
	}

}

