/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.references;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class AttributeReference {

	private final NameAdapter name;
	private final IEvaluatedType parent;
	private final SimpleNode node;
	private final Module module;

	public AttributeReference(NameAdapter name, IEvaluatedType parent, SimpleNode node, Module module) {
		this.name = name;
		this.parent = parent;
		this.node = node;
		this.module = module;
	}
	
	/* (non-Javadoc)
	 * @see ch.hsr.ifs.pystructure.typeinference.results.references.IThing#getName()
	 */
	public NameAdapter getName() {
		return name;
	}

	public IEvaluatedType getParent() {
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
