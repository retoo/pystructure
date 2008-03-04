/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.io.File;


public class ImportPath extends Package {

	public ImportPath(File workspace, String path, IModuleCreator runtime) {
		super("syspath", workspace, path, null, runtime);
	}

	@Override
	protected void registerIn(Path p) {
		/* do nothing */
	}

	@Override
	public Package getParent() {
		throw new RuntimeException("Trying to get parent of SysPath Entry");
	}

}
