/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.FunctionDef;
import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;

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
