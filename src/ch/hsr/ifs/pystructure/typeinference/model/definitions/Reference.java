/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;

public class Reference {

	private final Definition definition;
	private final SimpleNode node;

	public Reference(Definition definition, SimpleNode node) {
		this.definition = definition;
		this.node = node;
	}
	
	public SimpleNode getNode() {
		return node;
	}
	
	public Definition getDefinition() {
		return definition;
	}

	@Override
	public String toString() {
		return "Reference of " + definition + " at " + NodeUtils.nodePosition(node) + " node " + node;
	}
}
