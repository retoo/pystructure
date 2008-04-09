/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.visitors;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElementContainer;
import ch.hsr.ifs.pystructure.utils.IterableIterator;
import ch.hsr.ifs.pystructure.utils.IteratorChain;
import ch.hsr.ifs.pystructure.utils.ListUtils;

public class Workspace {

	private final List<SourceFolder> sourceFolders;

	public Workspace(File sourceDir) {
		this(ListUtils.wrap(sourceDir));
	}
	
	public Workspace(List<File> sourceDirs) {
		this.sourceFolders = new LinkedList<SourceFolder>();
		
		for (File sourceDir : sourceDirs) {
			SourceFolder sourceFolder = new SourceFolder(sourceDir);
			sourceFolder.traverse();
			sourceFolders.add(sourceFolder);
		}
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

	public Module getModule(String name) {
		for (SourceFolder sourceFolder : sourceFolders) {
			PathElement child = sourceFolder.getChild(name);
			if (child instanceof Module) {
				return (Module) child;
			}
		}
		throw new RuntimeException("Unable to get module " + name);
	}

	public Iterable<Module> getModules() {
		IteratorChain<Module> iterator = new IteratorChain<Module>();
		for (SourceFolder sourceFolder : sourceFolders) {
			iterator.add(sourceFolder.getModules());
		}
		return new IterableIterator<Module>(iterator);
	}
	
	@Deprecated
	public Module getModule(File file) {
		for (SourceFolder sourceFolder : sourceFolders) {
			Module module = sourceFolder.getModule(file);
			if (module != null) {
				return module;
			}
		}

		throw new RuntimeException("Unable to get module " + file);
	}

}
