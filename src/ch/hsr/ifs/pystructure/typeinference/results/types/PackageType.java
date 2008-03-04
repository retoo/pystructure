/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.dltk.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.dltk.types.IClassType;
import ch.hsr.ifs.pystructure.typeinference.dltk.types.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;

public class PackageType extends ClassType implements IClassType {

	private String modelKey;
	private Package pkg;

	public PackageType(Package pkg) {
		this.pkg = pkg;
		this.modelKey = pkg.getName().getId();
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
	
	public Package getPackage() {
		return pkg;
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
		final PackageType other = (PackageType) obj;
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
