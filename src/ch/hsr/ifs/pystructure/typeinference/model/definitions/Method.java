/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.List;

import org.python.pydev.parser.jython.ast.FunctionDef;

/**
 * Definition of a method of a class.
 */
public class Method extends Function {

	private final Class klass;
	public List<? extends Reference> references;

	public Method(Module module, String name, FunctionDef functionDef, Class klass) {
		super(module, name, functionDef, klass);
		this.klass = klass;
		this.references = null;
	}
	
	@Override
	public String toString() {
		return "Method " + getName() + " " + getNodePosition();
	}
	
	@Override
	public String getDescription() {
		return "method '" + getName() + "' of " + klass.getDescription();
	}

	public Class getKlass() {
		return klass;
	}
}
