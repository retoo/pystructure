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

import java.util.List;

import org.python.pydev.parser.jython.ast.FunctionDef;

/**
 * Definition of a method of a class.
 */
public class Method extends Function {

	private final Class klass;
	public List<? extends Reference> references;

	public Method(Module module, String name, FunctionDef functionDef, Class klass) {
		super(module, name, functionDef);
		this.klass = klass;
		this.references = null;
	}
	
	@Override
	public String toString() {
		return "Method " + getName() + " " + getNodePosition();
	}
	
	@Override
	public String getDescription() {
		return "method '" + getName() + "' of " + klass.getDescription();
	}

	public Class getKlass() {
		return klass;
	}

}
