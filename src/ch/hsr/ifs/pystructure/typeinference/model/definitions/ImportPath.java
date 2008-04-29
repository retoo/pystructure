package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;

/**
 * Represents a path which is used for imports from a module. Corresponds to
 * "pkg.module" in "from pkg.module import Class" imports.
 */
public class ImportPath {
	
	private final Module module;
	private final NamePath namePath;
	private final int level;

	/**
	 * Constructs a new import path.
	 * 
	 * @param module where the import is done from
	 * @param namePath path of the import
	 * @param level for relative imports
	 */
	public ImportPath(Module module, NamePath namePath, int level) {
		this.module = module;
		this.namePath = namePath;
		this.level = level;
	}
	
	public boolean isRelative() {
		return level != 0;
	}

	public Module getModule() {
		return module;
	}

	public NamePath getNamePath() {
		return namePath;
	}

	public int getLevel() {
		return level;
	}
	
	@Override
	public String toString() {
		return namePath + " imported from module " + module + (level != 0 ? " with level" + level : "");
	}
	
}
