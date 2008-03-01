/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.results.types;

import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;
import org.python.pydev.refactoring.typeinference.model.definitions.Function;
import org.python.pydev.refactoring.typeinference.model.definitions.Module;

public class FunctionType implements IEvaluatedType {

	private Module module;
	private Function function;
	
	public FunctionType(Module module, Function function) {
		this.module = module;
		this.function = function;
	}
	
	public Module getModule() {
		return module;
	}
	
	public Function getFunction() {
		return function;
	}
	
	public String getTypeName() {
		return "function";
	}

	public boolean subtypeOf(IEvaluatedType type) {
		return false;
	}
}
