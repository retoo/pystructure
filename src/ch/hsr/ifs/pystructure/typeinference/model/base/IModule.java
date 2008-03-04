/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.base;

import java.io.File;

public interface IModule {
	
	File getFile();
	String getRelativePath();
//	IDocument getDocument();
	
}
