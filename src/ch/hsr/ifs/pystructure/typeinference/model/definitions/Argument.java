/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.exprType;

/**
 * Definition of an argument in the signature of a function or method.
 */
public class Argument extends Definition {
	
	private final int position;
	private final exprType defaultValue;
	private final Function function;
	
	public Argument(Module module, String name, exprType argument, int position, exprType defaultValue, Function function) {
		super(module, name, argument);
		this.position = position;
		this.defaultValue = defaultValue;
		this.function = function;
	}
	
	public String toString() {
		return "Argument " + getName() + " of " + getFunction();
	}

	public int getPosition() {
		return position;
	}

	public exprType getDefaultValue() {
		return defaultValue;
	}
	
	public Function getFunction() {
		return function;
	}

}
