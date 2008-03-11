/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.references;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.dltk.types.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
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
		return "attribute reference '" + name + "' of class " + getParent().getTypeName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AttributeReference other = (AttributeReference) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (node == null) {
			if (other.node != null) {
				return false;
			}
		} else if (!node.equals(other.node)) {
			return false;
		}
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		return true;
	}

}
