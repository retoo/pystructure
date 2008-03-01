/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.For;
import org.python.pydev.refactoring.typeinference.model.base.IModule;
import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;

/**
 * Definition of a loop variable by a for loop.
 */
public class LoopVariableDefinition extends Definition<For> {

	public LoopVariableDefinition(IModule module, NameAdapter name, For loop) {
		super(module, name, loop);
	}
	
}
