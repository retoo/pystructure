/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.scopes;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;

public class Scope extends Block {
	private Definition definition;
	private List<String> globals;
	
	public Scope(Block parent, Definition definition) {
		super(parent);
		this.definition = definition;
		this.globals = new ArrayList<String>();
	}
	
	public void setGlobal(String name) {
		globals.add(name);
	}
	
	public boolean isGlobal(String name) {
		return globals.contains(name);
	}
	
	public Definition getDefinition() {
		return definition;
	}
	
	@Override
	protected List<Definition> getParentDefinitions(String name) {
		return getParent().getAllDefinitions(name);
	}
}
