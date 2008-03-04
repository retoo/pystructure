/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.FunctionDef;

import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

/**
 * Definition of a method of a class.
 */
public class Method extends Function {

	private final Class klass;

	public Method(Module module, NameAdapter name, FunctionDef functionDef, Class klass) {
		super(module, name, functionDef, klass);
		this.klass = klass;
	}
	
	@Override
	public String toString() {
		return "Method " + getName() + " defined at line " + getNode().beginLine + ", column " + getNode().beginColumn;
	}
	
	@Override
	public String getDescription() {
		return "method '" + getName() + "' of " + klass.getDescription();
	}

	public Class getKlass() {
		return klass;
	}
}
