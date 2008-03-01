/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.Assign;
import org.python.pydev.refactoring.typeinference.model.base.IModule;
import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;

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
