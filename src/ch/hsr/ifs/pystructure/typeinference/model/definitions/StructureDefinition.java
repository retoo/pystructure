/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.SimpleNode;

public abstract class StructureDefinition extends Definition {
	
	private final List<StructureDefinition> children;
	
	public StructureDefinition() {
		children = new ArrayList<StructureDefinition>();
	}
	
	public StructureDefinition(Module module, String name, SimpleNode node) {
		super(module, name, node);
		children = new ArrayList<StructureDefinition>();
	}
	
	public void addChild(StructureDefinition child) {
		children.add(child);
	}
	
	public List<StructureDefinition> getChildren() {
		return children;
	}
	
}
