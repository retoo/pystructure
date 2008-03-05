/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.LinkedList;
import java.util.List;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.model.base.IModule;
import ch.hsr.ifs.pystructure.typeinference.model.base.IThing;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

/**
 * Definition of a name, for example through an assignment, a def or a class
 * statement. Each definition has a scope (function, class, module) and a node.
 */
public abstract class Definition<NodeType extends SimpleNode> implements IThing {

	private NameAdapter name;
	private NodeType node;
	private LinkedList<NameUse> uses;
	private IModule module;

	protected Definition() {
		this.uses = new LinkedList<NameUse>();
	}
	
	public Definition(IModule module, NameAdapter name, NodeType node) {
		this();
		init(module, name, node);
	}
	
	protected void init(IModule module, NameAdapter name, NodeType node) {
		this.module = module;
		this.name = name;
		this.node = node;
	}
	
	public NameAdapter getName() {
		return name;
	}
	
	// TODO: Decide whether the generic node is useful at all?
	public NodeType getNode() {
		return node;
	}
	
	public void addUse(NameUse nameUse) {
		uses.add(nameUse);
	}

	public List<NameUse> getUses() {
		return uses;
	}
	
	public IModule getModule() {
		return module;
	}
	
	public String getDescription() {
		return toString();
	}
	
	public String getNodePosition() {
		return "(L" + getNode().beginLine + " C" + getNode().beginColumn + ")";
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
