/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.references;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;

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

}
