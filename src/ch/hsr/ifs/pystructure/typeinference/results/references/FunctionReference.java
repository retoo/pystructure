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

import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.jython.ast.keywordType;

import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Argument;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Reference;

public class FunctionReference extends Reference {
	
	public FunctionReference(Function definition, exprType expression) {
		super(definition, expression);
	}
	
	public exprType getArgumentExpression(Argument argument) {
		return getArgumentExpression(argument, false);
	}
	
	/**
	 * Given a Call node, extract the expression which is assigned to this argument.
	 * @param call
	 * @return argument expression
	 */
	protected exprType getArgumentExpression(Argument argument, boolean firstArgumentIsImplicit) {
		if (!(getExpression().parent instanceof Call)) {
			return null;
		}
		Call call = (Call) getExpression().parent;
		if (call.func != getExpression()) {
			return null;
		}

		int position = argument.getPosition();
		if (firstArgumentIsImplicit) {
			// The first argument (self) does not occur in the call argument
			// list, so adjust the position.
			position -= 1;
		}

		// Is it passed as a positional argument?
		if (position < call.args.length) {
			return call.args[position];
		}

		// Is it passed as a keyword argument?
		for (keywordType keyword : call.keywords) {
			String keywordName = NodeUtils.getId(keyword.arg);
			if (keywordName.equals(argument.getName())) {
				return keyword.value;
			}
		}

		// Return default value, may also return null.
		return argument.getDefaultValue();
	}
}
