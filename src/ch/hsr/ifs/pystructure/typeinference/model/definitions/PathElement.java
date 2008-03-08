/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

public interface PathElement {

	/**
	 * Human readable name of the path element. For modules this is the module
	 * name, for packages the actual package name.
	 * 
	 * @return
	 */
	NameAdapter getName();

}