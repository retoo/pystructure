/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.definitions;

import java.util.LinkedList;

public class Path {
	private LinkedList<PathElement> path;

	public Path() {
		path = new LinkedList<PathElement>();
	}
	
	public void add(PathElement e) {
		path.add(e);
	}
	
	public PathElement base() {
		return path.getFirst();
	}
	
	public PathElement top() {
		return path.getLast();
	}
	
	@Override
	public String toString() {
		return path.toString();
	}
}
