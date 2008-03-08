/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.visitors;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.IModuleCreator;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.IPackage;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportPath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;

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
	 * @see ch.hsr.ifs.pystructure.typeinference.model.definitions.IModuleCreator#createModule(java.io.File, ch.hsr.ifs.pystructure.typeinference.model.definitions.IPackage)
	 */
	public Module createModule(File file, String relativePath, IPackage pkg) {
		Module module = new Module(file, relativePath,  pkg);
		prepareModule(module);
		return module;	
	}
	
	/* (non-Javadoc)
	 * @see ch.hsr.ifs.pystructure.typeinference.model.definitions.IModuleCreator#createModule(java.lang.String, java.lang.String, ch.hsr.ifs.pystructure.typeinference.model.definitions.IPackage)
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