/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.base;

import java.io.File;

public interface IModule {
	
	File getFile();
	String getRelativePath();
//	IDocument getDocument();
	
}
