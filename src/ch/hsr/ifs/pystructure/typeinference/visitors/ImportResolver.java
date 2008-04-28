package ch.hsr.ifs.pystructure.typeinference.visitors;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportPath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElementContainer;

/**
 * Provides various methods for resolving imports of modules or packages.
 */
public class ImportResolver {

	private final List<SourceFolder> sourceFolders;

	public ImportResolver(List<SourceFolder> sourceFolders) {
		this.sourceFolders = sourceFolders;
	}

	/**
	 * Resolve the specified import path.
	 * 
	 * @return the resulting element
	 */
	public PathElement resolve(ImportPath importPath) {
		/* first we try to look if it is a relative lookup */
		PathElementContainer parent = importPath.getModule().getParent();
		
		if (importPath.isRelative() && importPath.getLevel() > 1) {
			for (int i = 1; i < importPath.getLevel(); i++) {
				if (parent == null) {
					throw new RuntimeException("Relative import beyond toplevel package.");
				}
				parent = parent.getParent();
			}
		}
		
		if (importPath.isRelative() && !(parent instanceof Package)) {
			throw new RuntimeException("Relative import not inside package.");
		}
		
		PathElement result = this.resolve(importPath.getNamePath(), parent);
		if (result != null) {
			return result;
		}

		if (importPath.isRelative()) {
			throw new RuntimeException("Invalid relative import.");
		}
		
		/* Search absolute in all source folders */
		result = this.resolve(importPath.getNamePath());
		
		/* If result is null, the import failed. A warning might be useful here. */
		
		return result;
	}

	/**
	 * Resolve the import of path by descending into parent.
	 * 
	 * @param path the path to resolve
	 * @param parent could be a SourceFolder or a Package
	 * @return the resulting element or null if nothing was found
	 */
	private PathElement resolve(NamePath path, PathElementContainer parent) {
		List<String> parts = path.getParts();
		int remaining = parts.size();
		
		PathElementContainer pkg = parent;
		
		for (String part : parts) {
			PathElement child = pkg.getChild(part);
			--remaining;
			
			if (remaining == 0) {
				return child;
			} else if (child instanceof Package) {
				pkg = (Package) child;
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * Resolve the absolute import of path, searching the whole workspace.
	 * 
	 * @param path
	 * @return
	 */
	private PathElement resolve(NamePath path) {
		String first = path.getFirstPart();

		for (SourceFolder sourceFolder : sourceFolders) {
			if (sourceFolder.getChild(first) != null) {
				return this.resolve(path, sourceFolder);
			}
		}
		
		return null;
	}

}
