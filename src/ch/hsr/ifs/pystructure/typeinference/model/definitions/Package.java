/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;

public class Package implements PathElement, PathElementContainer {
	
	private final NamePath namePath;
	private final PathElementContainer parent;
	
	private Map<String, PathElement> children;

	public Package(NamePath namePath, PathElementContainer parent, File dir, File initFile) {
		this.namePath = namePath;
		this.parent = parent;
		this.children = new HashMap<String, PathElement>();
	}
	
	public String getName() {
		return namePath.getLastPart();
	}
	
	public void addChild(PathElement child) {
		children.put(child.getName(), child);
	}
	
	public PathElement getChild(String name) {
		return children.get(name);
	}
	
	public NamePath getNamePath() {
		return namePath;
	}
	
	public PathElementContainer getParent() {
		return parent;
	}
	
}
