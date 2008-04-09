/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class ClassType extends AbstractType {
	private Module module;
	private Class klass;
	
	public ClassType(String typeName) {
		super(typeName);
		/* FIXME: shouldn't we use a different class for that? */
	}

	public ClassType(Module module, Class klass) {
		super(klass.getName());
		this.module = module;
		this.klass = klass;
	}
	
	public Module getModule() {
		return module;
	}
	
	public Class getKlass() {
		return klass;
	}

}
