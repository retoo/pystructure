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

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Reference;

public class AttributeReference extends Reference {

	private final String name;
	private final CombinedType parent;
	private final SimpleNode node;
	private final Module module;

	public AttributeReference(String name, CombinedType parent, SimpleNode node, Module module) {
		super(module, node);
		
		this.name = name;
		this.parent = parent;
		this.node = node;
		this.module = module;
	}
	
	/* (non-Javadoc)
	 * @see ch.hsr.ifs.pystructure.typeinference.results.references.IThing#getName()
	 */
	public String getName() {
		return name;
	}

	public CombinedType getParent() {
		return parent;
	}

	public SimpleNode getNode() {
		return node;
	}

	/* (non-Javadoc)
	 * @see ch.hsr.ifs.pystructure.typeinference.results.references.IThing#getModule()
	 */
	public Module getModule() {
		return module;
	}
	
	public String getDescription() {
		return "attribute reference '" + name + "' of class " + getParent().getTypeName() + " in " + module + " " + NodeUtils.nodePosition(node);
	}
	
	@Override
	public String toString() {
		return getDescription();
	}

}
