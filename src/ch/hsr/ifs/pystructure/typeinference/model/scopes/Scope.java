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

package ch.hsr.ifs.pystructure.typeinference.model.scopes;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;

public class Scope extends Block {
	private Definition definition;
	private List<String> globals;
	
	public Scope(Block parent, Definition definition) {
		super(parent);
		this.definition = definition;
		this.globals = new ArrayList<String>();
	}
	
	public void setGlobal(String name) {
		globals.add(name);
	}
	
	public boolean isGlobal(String name) {
		return globals.contains(name);
	}
	
	public Definition getDefinition() {
		return definition;
	}
	
	@Override
	protected List<Definition> getParentDefinitions(String name) {
		return getParent().getAllDefinitions(name);
	}
}
