/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.excepthandlerType;

/**
 * Definition of a variable by an except handler.
 */
public class ExceptDefinition extends Definition {
	
	public ExceptDefinition(Module module, String name, excepthandlerType handler) {
		super(module, name, handler);
	}
	
	public String toString() {
		return "Except variable " + getName() + " " + getNodePosition();
	}
}
