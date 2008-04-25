/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
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
