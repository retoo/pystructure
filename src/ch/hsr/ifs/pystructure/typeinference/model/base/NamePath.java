/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.base;

import java.util.List;

import ch.hsr.ifs.pystructure.utils.StringUtils;

public class NamePath {

	private static final String DOT = ".";
	private final String path;

	public NamePath(String name) {
		path = name;
	}

	public NamePath(NamePath parent, String child) {
		if (parent == null) {
			path = child;
		} else {
			path = parent.path + DOT + child;
		}
	}
	
	public List<String> getParts() {
		return StringUtils.dotSplitter(path);
	}

	public String getFirstPart() {
		return StringUtils.dotSplitter(path).getFirst();
	}

	public String getLastPart() {
		return StringUtils.dotSplitter(path).getLast();
	}
	
	@Override
	public String toString() {
		return path;
	}

}
