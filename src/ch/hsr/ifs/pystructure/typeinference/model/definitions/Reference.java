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

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;

public abstract class Reference {

	private final Definition definition;
	private final exprType expression;
	private final Module module;

	protected Reference(Definition definition, exprType expression, Module module) {
		this.definition = definition;
		this.expression = expression;
		this.module = module;
	}
	
	public exprType getExpression() {
		return expression;
	}
	
	public Definition getDefinition() {
		return definition;
	}
	
	public Module getModule() {
		return module;
	}

	@Override
	public String toString() {
		return "Reference of " + definition + " at " + NodeUtils.nodePosition(expression) + " node " + expression;
	}

}
