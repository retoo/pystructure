/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.contexts;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.results.references.FunctionReference;

/**
 * Context of a call. This makes it possible to find the type of an argument
 * in a context-sensitive manner:
 * 
 * <code>
 * def func(x):
 *     return x
 * 
 * func(1) ## type int
 * func(1.1) ## type float
 * </code>
 * 
 * Without the call context, both results would be float|int.
 */
public class CallContext extends PythonContext {

	private final FunctionReference functionReference;
	
	public CallContext(PythonContext parent, Module module, FunctionReference functionReference) {
		super(parent, module);
		this.functionReference = functionReference;
	}
	
	public FunctionReference getFunctionReference() {
		return functionReference;
	}

}
