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

package ch.hsr.ifs.pystructure.typeinference.results.types;

import org.python.pydev.parser.jython.ast.Tuple;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;

public class TupleType extends AbstractType {

	private final Tuple tuple;
	private final ModuleContext moduleContext;

	public TupleType(Tuple tuple, ModuleContext moduleContext) {
		super("tuple");
		this.tuple = tuple;
		this.moduleContext = moduleContext;
	}

	public Tuple getTuple() {
		return tuple;
	}

	public ModuleContext getContext() {
		return moduleContext;
	}

}
