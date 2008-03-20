/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;

public class PackageType extends AbstractType {

	private Package pkg;

	public PackageType(Package pkg) {
		super(pkg.getName().getId());
		this.pkg = pkg;
	}
	
	public boolean subtypeOf(IType type) {
		return false; //TODO
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
		result = prime * result	+ ((modelKey == null) ? 0 : modelKey.hashCode());
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
		final AbstractType other = (AbstractType) obj;
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
