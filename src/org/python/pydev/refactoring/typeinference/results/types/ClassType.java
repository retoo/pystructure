/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.python.pydev.refactoring.typeinference.results.types;

import org.python.pydev.refactoring.typeinference.dltk.types.IClassType;
import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;
import org.python.pydev.refactoring.typeinference.model.definitions.Class;
import org.python.pydev.refactoring.typeinference.model.definitions.Module;

public class ClassType implements IClassType {

	private Module module;
	private String modelKey;
	private Class klass;
	
	public ClassType(String modelKey) {
		this.modelKey = modelKey;
	}

	public ClassType(Module module, Class klass) {
		this.module = module;
		this.klass = klass;
		this.modelKey = klass.getName().getId();
	}
	
	public Module getModule() {
		return module;
	}
	
	public Class getKlass() {
		return klass;
	}

	public boolean subtypeOf(IEvaluatedType type) {
		return false; //TODO
	}

	public String getModelKey() {
		return modelKey;
	}
	
	public String getTypeName() {
		return getModelKey();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((modelKey == null) ? 0 : modelKey.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ClassType other = (ClassType) obj;
		if (modelKey == null) {
			if (other.modelKey != null) {
				return false;
			}
		} else if (!modelKey.equals(other.modelKey)) {
			return false;
		}
		return true;
	}
}
