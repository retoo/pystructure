/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class MethodType extends FunctionType {

	public MethodType(Module module, Method method) {
		super(module, method);
	}
	
	public Method getMethod() {
		return (Method) super.getFunction();
	}
	
	@Override
	public String getTypeName() {
		return "method";
	}

}
