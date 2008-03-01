/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.definitions;

import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;

public interface PathElement {

	/**
	 * Human readable name of the path element. For modules this is the module
	 * name, for packages the actual package name.
	 * 
	 * @return
	 */
	NameAdapter getName();

}
