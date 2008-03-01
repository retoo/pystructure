/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.results.types;

import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;
import org.python.pydev.refactoring.typeinference.model.definitions.Method;
import org.python.pydev.refactoring.typeinference.model.definitions.Module;

public class MethodType extends FunctionType {

	public MethodType(Module module, Method method) {
		super(module, method);
	}
	
	public Method getMethod() {
		return (Method) super.getFunction();
	}
	
	public String getTypeName() {
		return "method";
	}

	public boolean subtypeOf(IEvaluatedType type) {
		return false;
	}
}
