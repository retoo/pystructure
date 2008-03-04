/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.excepthandlerType;

import ch.hsr.ifs.pystructure.typeinference.model.base.IModule;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

/**
 * Definition of a variable by an except handler.
 */
public class ExceptDefinition extends Definition<excepthandlerType> {
	
	public ExceptDefinition(IModule module, NameAdapter name, excepthandlerType handler) {
		super(module, name, handler);
	}
	
	public String toString() {
		return "Except variable " + getName() + " defined at line " + getNode().beginLine + ", column " + getNode().beginColumn;
	}
}
