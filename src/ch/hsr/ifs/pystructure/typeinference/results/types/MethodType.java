/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

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

	public boolean subtypeOf(IType type) {
		return false;
	}
}
