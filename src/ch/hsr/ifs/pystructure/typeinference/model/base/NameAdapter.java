/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.base;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.parser.jython.ast.NameTok;
import org.python.pydev.parser.jython.ast.NameTokType;

/**
 * Adapter for Name nodes, because they can be of type Name or NameTok
 * (FunctionDef, ClassDef).
 */
public class NameAdapter implements Comparable<NameAdapter> {
	private SimpleNode node;
	private String name;

	public NameAdapter(Name node) {
		this.node = node;
		this.name = node.id;
	}
	
	public NameAdapter(NameTokType node) {
		this.node = node;
		this.name = ((NameTok) node).id;
	}
	
	public NameAdapter(String name) {
		this.node = null;
		this.name = name;
	}
	
	public SimpleNode getNode() {
		return node;
	}

	public int compareTo(NameAdapter other) {
		return getId().compareTo(other.getId());
	}
	
	public String getId() {
		return name;
	}
	
	public String toString() {
		return getId();
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
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
		final NameAdapter other = (NameAdapter) obj;
		if (getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!getId().equals(other.getId())) {
			return false;
		}
		return true;
	}
}
