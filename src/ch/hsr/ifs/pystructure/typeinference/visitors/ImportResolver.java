package ch.hsr.ifs.pystructure.typeinference.visitors;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElementContainer;

public class ImportResolver {

	private final List<SourceFolder> sourceFolders;

	public ImportResolver(List<SourceFolder> sourceFolders) {
		this.sourceFolders = sourceFolders;
	}

	public PathElement resolve(Module fromModule, NamePath path, int level) {
		/* first we try to look if it is a relative lookup */
		PathElementContainer parent = fromModule.getParent();

		boolean isRelativeImport = (level != 0);
		
		if (isRelativeImport && level > 1) {
			for (int i = 1; i < level; i++) {
				if (parent == null) {
					throw new RuntimeException("Relative Invalid relative import.");
				}
				parent = parent.getParent();
			}
		}
		
		if (isRelativeImport && !(parent instanceof Package)) {
			throw new RuntimeException("Relative import not inside package");
		}
		
		PathElement result = this.resolve(path, parent);
		if (result != null) {
			return result;
		}

		if (isRelativeImport) {
			throw new RuntimeException("Invalid relative import.");
		}
		
		/* Search absolute in all source folders */
		result = this.resolve(path);
		
		/* If result is null, the import failed. A warning might be useful here. */
		
		return result;
	}

	public PathElement resolve(NamePath path, PathElementContainer parent) {
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

	public PathElement resolve(NamePath path) {
		String first = path.getFirstPart();

		for (SourceFolder sourceFolder : sourceFolders) {
			if (sourceFolder.getChild(first) != null) {
				return this.resolve(path, sourceFolder);
			}
		}
		
		return null;
	}

}
