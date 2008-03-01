/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.base;


public interface IThing {
	
	NameAdapter getName();
	IModule getModule();
	String getDescription();

}
