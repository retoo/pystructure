/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.LinkedList;
import java.util.List;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;

/**
 * Definition of a name, for example through an assignment, a def or a class
 * statement. Each definition has a scope (function, class, module) and a node.
 */
public abstract class Definition {

	private String name;
	private SimpleNode  node;
	private LinkedList<NameUse> uses;
	private Module module;
	public CombinedType type;

	protected Definition() {
		this.uses = new LinkedList<NameUse>();
		this.type = null;
	}
	
	public Definition(Module module, String name, SimpleNode node) {
		this();
		init(module, name, node);
	}
	
	protected void init(Module module, String name, SimpleNode node) {
		this.module = module;
		this.name = name;
		this.node = node;
	}
	
	public String getName() {
		return name;
	}
	
	// TODO: Merge this with getNamePath or getFullName
	public String getUniqueIdentifier() {
		return String.valueOf(this.hashCode());
	}
	
	// TODO: Decide whether the generic node is useful at all?
	public SimpleNode getNode() {
		return node;
	}
	
	public void addUse(NameUse nameUse) {
		uses.add(nameUse);
	}

	public List<NameUse> getUses() {
		return uses;
	}
	
	public Module getModule() {
		return module;
	}
	
	public String getDescription() {
		return toString();
	}
	
	public String getNodePosition() {
		return NodeUtils.nodePosition(node);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
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
		final Definition other = (Definition) obj;
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
		return true;
	}
}
