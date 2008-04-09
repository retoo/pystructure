/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;

public interface PathElementContainer {

	NamePath getNamePath();
	
	void addChild(PathElement child);
	PathElement getChild(String name);
	PathElementContainer getParent();

}
