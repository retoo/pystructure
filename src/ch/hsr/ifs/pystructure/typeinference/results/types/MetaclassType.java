/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.basetype.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

public class MetaclassType extends AbstractType {

	private Module module;
	private Class klass;

	public MetaclassType(Module module, Class klass) {
		super("metaclass");
		this.module = module;
		this.klass = klass;
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

	public String getTypeName() {
		return getModelKey();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((klass == null) ? 0 : klass.hashCode());
		result = prime * result + ((module == null) ? 0 : module.hashCode());
		return result;
	}

	@Override
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
		final MetaclassType other = (MetaclassType) obj;
		if (klass == null) {
			if (other.klass != null) {
				return false;
			}
		} else if (!klass.equals(other.klass)) {
			return false;
		}
		if (module == null) {
			if (other.module != null) {
				return false;
			}
		} else if (!module.equals(other.module)) {
			return false;
		}
		return true;
	}

}
