/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.base;


public interface IThing {
	
	NameAdapter getName();
	IModule getModule();
	String getDescription();

}
