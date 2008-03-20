/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class ModuleType extends AbstractType {

	private Module module;

	public ModuleType(Module module) {
		super(module.getName().getId());
		this.module = module;
	}
	
	public boolean subtypeOf(IType type) {
		return false; //TODO
	}

	public String getTypeName() {
		return getModelKey();
	}
	
	public Module getModule() {
		return module;
	}

}
