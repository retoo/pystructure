/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.visitors;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.python.pydev.refactoring.typeinference.model.definitions.IModuleCreator;
import org.python.pydev.refactoring.typeinference.model.definitions.IPackage;
import org.python.pydev.refactoring.typeinference.model.definitions.ImportPath;
import org.python.pydev.refactoring.typeinference.model.definitions.Module;

/**
 * @author reto
 *
 */
public class Runtime implements IModuleCreator {

	private LinkedList<ImportPath> importPaths;

	public Runtime(File workspace, List<String> paths) {
		this.importPaths = new LinkedList<ImportPath>();
		
		for (String path : paths) {
			this.importPaths.add(new ImportPath(workspace, path, this));
		}
	}

	/* (non-Javadoc)
	 * @see org.python.pydev.refactoring.typeinference.model.definitions.IModuleCreator#createModule(java.io.File, org.python.pydev.refactoring.typeinference.model.definitions.IPackage)
	 */
	public Module createModule(File file, String relativePath, IPackage pkg) {
		Module module = new Module(file, relativePath,  pkg);
		prepareModule(module);
		return module;	
	}
	
	/* (non-Javadoc)
	 * @see org.python.pydev.refactoring.typeinference.model.definitions.IModuleCreator#createModule(java.lang.String, java.lang.String, org.python.pydev.refactoring.typeinference.model.definitions.IPackage)
	 */
	public Module createModule(String modulename, String source,
			IPackage pkg) {
		Module module = new Module(modulename, source, pkg);
		prepareModule(module);
		return module;	
	}

	private void prepareModule(Module module) {
		DefinitionVisitor visitor = new DefinitionVisitor(importPaths, module);
		visitor.run();
	}

	public void registerImportPath(ImportPath importPath) {
		importPaths.add(importPath);
	}

	public ImportPath createImportPath(File workspaceDir, String path) {
		return new ImportPath(workspaceDir, path, this);
	}


}
