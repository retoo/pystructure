/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;


public interface IPackage {

	Path lookFor(String string);
	IPackage getParent();
	NameAdapter getName();
	
}
