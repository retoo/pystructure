package ch.hsr.ifs.pystructure.typeinference.model.base;

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
	
	public String getLastPart() {
		return StringUtils.dotSplitter(path).getLast();
	}
	
	@Override
	public String toString() {
		return path;
	}

}
