/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.definitions;

import java.io.File;


public interface IModuleCreator {

	Module createModule(File file, String relativePath, IPackage pkg);
	Module createModule(String modulename, String source,
			IPackage importPathStub);

}
