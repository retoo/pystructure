/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.definitions;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.refactoring.typeinference.model.base.IThing;
import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;

public abstract class Use implements IThing {

	private final NameAdapter name;
	private final SimpleNode node;
	private final Module module;

	public Use(NameAdapter name, SimpleNode node, Module module) {
		this.name = name;
		this.node = node;
		this.module = module;
	}

	public NameAdapter getName() {
		return name;
	}
	
	public SimpleNode getNode() {
		return node;
	}

	public Module getModule() {
		return module;
	}
	
	public String getDescription() {
		return "'" + getName().toString() + "'";
	}

}
