/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;


import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.utils.StringUtils;

public class Package implements IPackage, PathElement {
	private File workspacePath;
	private String relativePath;
	private NameAdapter name;
	
	private File path;
	
	private Package parent;
	private HashMap<String, PathElement> children;

	protected IModuleCreator runtime;

	public Package(String name, File workspacePath, String relPath, Package parent, IModuleCreator runtime) {
		this.name = new NameAdapter(name);
		this.relativePath = relPath;
		
		this.workspacePath = workspacePath;
		this.path = new File(workspacePath, relPath);
		
		this.parent = parent;
		this.runtime = runtime;

		this.children = new HashMap<String, PathElement>();
	}

	public Path lookFor(String targetString) {
		LinkedList<String> parts = StringUtils.dotSplitter(targetString);
		
		return lookFor(parts);
	}
	
	public Path lookFor(LinkedList<String> parts) {
		Path path = new Path();
		
		/* Divide the module path into its parts */
		boolean foundSomething = lookIn(parts, path, false);
		
		if (foundSomething) {
			return path;
		} else {
			return null;
		}
	}
	
	private boolean lookIn(LinkedList<String> target, Path resultPath, boolean abortIfNotFound) {
		String targetString = target.toString();
		
		registerIn(resultPath);
		
		if (target.isEmpty()) {
			return true;
		}

		String childName = target.poll();

		PathElement childDefinition = getChild(childName);

		if (childDefinition != null) {
			if (childDefinition instanceof Package) {
				Package child = (Package) childDefinition;

				return child.lookIn(target, resultPath, abortIfNotFound);
			} else if (childDefinition instanceof Module) {
				Module module = (Module) childDefinition;
				
				resultPath.add(module);
				
				return true;
			} else {
				throw new RuntimeException("Found a neither a package nor a module while solving " + targetString);
			} 
		} else {
			
			File childPath = checkFile(workspacePath, relativePath, childName);
			
			if (childPath != null) {
				String childRelativePath = relativePath + File.separator + childPath.getName();
				if (childPath.isDirectory()) {
					Package pkg = new Package(childName, workspacePath, childRelativePath, this, runtime);
					this.registerChild(pkg);
					
					return pkg.lookIn(target, resultPath, abortIfNotFound);
				} else {
					/* Make new module */
					Module module = runtime.createModule(workspacePath, childRelativePath, this);
					this.registerChild(module);
					
					resultPath.add(module);
					
					return true;
				}
			} else {
				if (abortIfNotFound) {
					throw new RuntimeException("Unable to import " + targetString);
				} else {
					return false;
				}
			}
		}
	}


	protected void registerIn(Path resultPath) {
		resultPath.add(this);
	}

	protected void registerChild(PathElement element) {
		children.put(element.getName().getId(), element);
	}
	
	public PathElement getChild(NameAdapter attribName) {
		return children.get(attribName.getId()); /* TODO */
	}
	
	@Deprecated
	private PathElement getChild(String childName) {
		return children.get(childName);
	}

	private static File checkFile(File workspace, String path, String filename) {
		File base = new File(workspace, path);
		
		File file = new File(base, filename);
		if (file.exists()) {
			return file;
		}

		file = new File(base, filename + ".py");
		if (file.exists()) {
			return file;
		} 

		return null;
	}

	public NameAdapter getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see ch.hsr.ifs.pystructure.typeinference.model.definitions.PackageInterface#getParent()
	 */
	public Package getParent() {
		return parent;
	}
	
	public File getPath() {
		return path;
	}

	@Override
	public String toString() {
		return "Package " + name + " (" + relativePath +  ")";
	}
	
}
