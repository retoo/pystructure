/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.results.types;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;

public class PackageType extends AbstractType {

	private Package pkg;

	public PackageType(Package pkg) {
		super(pkg.getName());
		this.pkg = pkg;
	}
	
	public Package getPackage() {
		return pkg;
	}

}
