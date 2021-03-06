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

package ch.hsr.ifs.pystructure.typeinference.results.references;

import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class MethodReference extends FunctionReference {

	private boolean firstArgumentIsImplicit;

	public MethodReference(Method method, exprType expression, Module module) {
		this(method, expression, module, true);
	}
	
	public MethodReference(Method method, exprType expression, Module module, boolean firstArgumentIsImplicit) {
		super(method, expression, module);
		this.firstArgumentIsImplicit = firstArgumentIsImplicit;
	}

	@Override
	public exprType getArgumentExpression(Argument argument) {
		if (argument.getPosition() == 0 && firstArgumentIsImplicit) {
			// The self argument is wanted in a call like this:
			//   instance.method(argument)
			// ... which is "instance".
			if (getExpression() instanceof Attribute) {
				Attribute attribute = (Attribute) getExpression();
				return attribute.value;
			} else {
				throw new RuntimeException("Could not get first argument expression.");
			}
		}
		
		return super.getArgumentExpression(argument, firstArgumentIsImplicit);
	}

}
