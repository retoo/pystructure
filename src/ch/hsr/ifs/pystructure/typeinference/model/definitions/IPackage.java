/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

public interface IPackage {

	Path lookFor(String string);
	IPackage getParent();
	String getName();
	
}
