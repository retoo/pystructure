package ch.hsr.ifs.pystructure.typeinference.visitors;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Package;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElement;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.PathElementContainer;

public class SourceFolder implements PathElementContainer {
	
	private static final String PYTHON_FILES = ".*\\.py";
	private final File sourceDirectory;
	private final Map<String, PathElement> children;
	private final Map<File, Module> filesToModules;
	
	public SourceFolder(File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
		this.children = new HashMap<String, PathElement>();
		this.filesToModules = new HashMap<File, Module>();
	}

	public static void main(String[] args) {
		File path = new File("s101g/examples/pydoku/");
		SourceFolder sourceDir = new SourceFolder(path);
		sourceDir.traverse();
	}
	
	public PathElement getChild(String name) {
		return children.get(name);
	}
	
	public PathElementContainer getParent() {
		return null;
	}
	
	public Module getModule(File file) {
		return filesToModules.get(file);
	}
	
	public Collection<Module> getModules() {
		return filesToModules.values();
	}
	
	public void traverse() {
		if (sourceDirectory.isDirectory()) {
			for (File file : sourceDirectory.listFiles()) {
				traverse(file, null, this);
			}
		}
	}
	
	private void traverse(File file, NamePath parentName, PathElementContainer parent) {
		String fileName = file.getName();
		
		if (file.isFile()) {
			if (fileName.matches(PYTHON_FILES)) {
				String name = fileName.replaceAll("\\.py$", "");
				NamePath namePath = new NamePath(parentName, name);
				addModule(namePath, parent, file);
			}
			
		} else if (file.isDirectory()) {
			File initFile = new File(file, "__init__.py");
			if (initFile.exists()) {
				NamePath namePath = new NamePath(parentName, fileName);
				Package pkg = addPackage(namePath, parent, file, initFile);
				
				for (File child : file.listFiles()) {
					if (child.equals(initFile)) {
						// Don't register __init__.py files as modules
						continue;
					}
					traverse(child, namePath, pkg);
				}
			}
		}
	}
	
	private void addModule(NamePath namePath, PathElementContainer parent, File file) {
		Module module = new Module(namePath, parent, file);
		
		StructureDefinitionVisitor structureDefinitionVisitor = new StructureDefinitionVisitor();
		structureDefinitionVisitor.run(module);
		DefinitionVisitor definitionVisitor = new DefinitionVisitor(module);
		definitionVisitor.run();
		
		parent.addChild(module);
		filesToModules.put(file, module);
	}

	private Package addPackage(NamePath namePath, PathElementContainer parent, File dir, File initFile) {
		Package pkg = new Package(namePath, parent, dir, initFile);
		
		parent.addChild(pkg);
		return pkg;
	}
	
	public void addChild(PathElement child) {
		this.children.put(child.getName(), child);
	}

}
