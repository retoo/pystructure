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
import java.util.regex.Pattern;

import org.python.pydev.refactoring.typeinference.model.definitions.ImportPath;
import org.python.pydev.refactoring.typeinference.model.definitions.Module;
import org.python.pydev.refactoring.typeinference.model.definitions.Path;
import org.python.pydev.refactoring.typeinference.model.definitions.PathElement;
import org.python.pydev.refactoring.utils.DirectoryTraverser;
import org.python.pydev.refactoring.utils.FileUtils;
import org.python.pydev.refactoring.utils.ListUtils;
import org.python.pydev.refactoring.utils.StringUtils;

public class Workspace {

	private static final Pattern PYTHON_FILES = Pattern.compile(".*\\.py");
	protected Runtime runtime;
	protected LinkedList<Module> modules;

	public Workspace(String workspacePath, List<String> importPaths) {
		this(new File(workspacePath), ListUtils.wrap(""), importPaths);
	}
	
	public Workspace(File workspaceDir, List<String> sourcefolders, List<String> importPaths) {
		this.modules = new LinkedList<Module>();
		this.runtime = new Runtime(workspaceDir, importPaths);

		for (String sourceFolder : sourcefolders) {
			ImportPath importPath = runtime.createImportPath(workspaceDir, sourceFolder);
			runtime.registerImportPath(importPath);
			
			File srcDir = new File(workspaceDir, sourceFolder);
			DirectoryTraverser directoryTraverser = new DirectoryTraverser(srcDir, PYTHON_FILES);
			List<String> files = directoryTraverser.getAllFiles();
			for (String moduleFile : files) {
				String modulePath = FileUtils.stripExtension(moduleFile);
				LinkedList<String> parts = StringUtils.slashSplitter(modulePath);
				
				Path result = importPath.lookFor(parts);
				
				if (result == null) {
					throw new RuntimeException("Inernal error, was uanble to resolve " + parts + " while initialising the workspace");
				}
				
				PathElement element = result.top();
				
				if (element instanceof Module) {
					Module module = (Module) element;
					
					modules.add(module);
				} else {
					/* internal error */
					throw new RuntimeException("Loading a file in the workspace didn't yield a module");
				}
			}
		}
	}

	@Deprecated
	public Module getModule(String path) {
		throw new RuntimeException("Deprecated");
	}

	public List<Module> getModules() {
		return modules;
	}

	// TODO: Use Map?
	public Module getModule(File file) {
		for (Module module : getModules()) {
			File moduleFile = module.getFile();
			if (file.equals(moduleFile)) {
				return module;
			}
		}
		throw new RuntimeException("Unable to get module " + file);
	}

}
