/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.For;

import ch.hsr.ifs.pystructure.typeinference.model.base.IModule;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

/**
 * Definition of a loop variable by a for loop.
 */
public class LoopVariableDefinition extends Definition<For> {

	public LoopVariableDefinition(IModule module, NameAdapter name, For loop) {
		super(module, name, loop);
	}
	
}
