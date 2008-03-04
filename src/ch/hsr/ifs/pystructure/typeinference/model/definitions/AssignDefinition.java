/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.Assign;

import ch.hsr.ifs.pystructure.typeinference.model.base.IModule;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

/**
 * Definition of a variable by an assignment.
 */
public class AssignDefinition extends Definition<Assign> {

	private final Value value;
	
	public AssignDefinition(IModule module, NameAdapter name, Assign assign, Value value) {
		super(module, name, assign);
		this.value = value;
	}
	
	public String toString() {
		return "Variable " + getName() + " defined at line " + getNode().beginLine + ", column " + getNode().beginColumn;
	}

	public Value getValue() {
		return value;
	}
}
