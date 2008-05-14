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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.utils.IterableIterator;
import ch.hsr.ifs.pystructure.utils.IteratorChain;
import ch.hsr.ifs.pystructure.utils.ListUtils;

public class Workspace {

	private static final File STDLIB_DIR = new File("resources/python-stdlib");

	private final List<SourceFolder> sourceFolders;
	private final ImportResolver importResolver;
	private final Module builtinModule;

	private Map<String, List<AttributeReference>> possibleAttributeReferences;

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
		
		SourceFolder stdlib = new SourceFolder(STDLIB_DIR);
		stdlib.traverse();
		sourceFolders.add(stdlib);
		
		this.builtinModule = (Module) stdlib.getChild("builtins");
		
		this.importResolver = new ImportResolver(sourceFolders);
		this.possibleAttributeReferences = new HashMap<String, List<AttributeReference>>();
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

	public ImportResolver getImportResolver() {
		return importResolver;
	}
	
	public Module getBuiltinModule() {
		return builtinModule;
	}

	public void addPossibleAttributeReference(AttributeReference attributeReference) {
		String name = attributeReference.getName();
		List<AttributeReference> list = possibleAttributeReferences.get(name);
		if (list == null) {
			list = new LinkedList<AttributeReference>();
			possibleAttributeReferences.put(name, list);
		}
		list.add(attributeReference);
	}
	
	public List<AttributeReference> getPossibleAttributeReferences(String name) {
		return possibleAttributeReferences.get(name);
	}

}
