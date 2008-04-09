/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.SimpleNode;

public abstract class Use {

	private final String name;
	private final SimpleNode node;
	private final Module module;

	public Use(String name, SimpleNode node, Module module) {
		this.name = name;
		this.node = node;
		this.module = module;
	}

	public String getName() {
		return name;
	}
	
	public SimpleNode getNode() {
		return node;
	}

	public Module getModule() {
		return module;
	}
	
	public String getDescription() {
		return "'" + getName() + "'";
	}

}
