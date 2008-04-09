/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
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
