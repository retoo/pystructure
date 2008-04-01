/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Function;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class FunctionType extends AbstractType {

	private Module module;
	private Function function;
	
	public FunctionType(Module module, Function function) {
		super("function");
		this.module = module;
		this.function = function;
	}
	
	public Module getModule() {
		return module;
	}
	
	public Function getFunction() {
		return function;
	}
	
}
