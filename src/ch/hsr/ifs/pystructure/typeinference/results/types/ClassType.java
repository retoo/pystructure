/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class ClassType extends AbstractType {

	private Module module;
	private Class klass;
	
	public ClassType(String modelKey) {
		super(modelKey);
	}

	public ClassType(Module module, Class klass) {
		super(klass.getName().getId());
		this.module = module;
		this.klass = klass;
	}
	
	public Module getModule() {
		return module;
	}
	
	public Class getKlass() {
		return klass;
	}

	public boolean subtypeOf(IType type) {
		return false; //TODO
	}

	public String getTypeName() {
		return getModelKey();
	}

}
