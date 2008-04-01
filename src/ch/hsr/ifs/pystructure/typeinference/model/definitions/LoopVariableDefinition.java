/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.For;

/**
 * Definition of a loop variable by a for loop.
 */
public class LoopVariableDefinition extends Definition {

	public LoopVariableDefinition(Module module, String name, For loop) {
		super(module, name, loop);
	}
	
}
