/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ParseException;

import ch.hsr.ifs.pystructure.parser.Parser;
import ch.hsr.ifs.pystructure.typeinference.model.base.IModule;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.utils.FileUtils;

public class Module extends Definition implements PathElement, IModule {

	private String path;
	private ArrayList<Use> containedUses;
	private ArrayList<Definition> definitions;
	private IPackage pkg;
	private File file;
	private String relativePath;
	private String source;

	public Module(File workspace, String relativePath, IPackage pkg) {
		this.relativePath = relativePath;
		this.file = new File(workspace, relativePath);
		this.path = file.getPath();
			
		try {
			String source = FileUtils.read(file);
			String name = FileUtils.stripExtension(file.getName());
			init(pkg, source, name);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void init(IPackage pkg, String source, String name) {
		this.pkg = pkg;

		this.containedUses = new ArrayList<Use>();
		this.definitions  = new ArrayList<Definition>();
		this.source = source;

		org.python.pydev.parser.jython.ast.Module module;
		try {
			module = Parser.parse(source);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		super.init(this, new NameAdapter(name), module);
	}

	// TODO: Should return all possible definitions, not just one.
	public Definition getChild(NameAdapter nameAdapter) {
		for (Definition definition : definitions) {
			if (definition.getName().equals(nameAdapter)) {
				return definition;
			}
		}

		throw new RuntimeException("Object " + nameAdapter + " not defined in " + this);
	}

	public String getPath() {
		return path;
	}
	
	// TODO: Package also needs a getFullName()
	public String getFullName() {
		StringBuilder s = new StringBuilder();
		s.append(getName());
		for (IPackage pkg = this.pkg; !(pkg instanceof ImportPath); pkg = pkg.getParent()) {
			s.insert(0, pkg.getName().getId() + ".");
		}
		return s.toString();
	}

	@Override
	public String toString() {
		return getName().getId();
	}

	public List<Use> getContainedUses() {
		return containedUses;
	}

	public List<Definition> getDefinitions() {
		return definitions;
	}
	
	public List<Class> getClasses() {
		List<Class> classes = new ArrayList<Class>();
		for (Definition definition : definitions) {
			if (definition instanceof Class) {
				classes.add((Class) definition);
			}
		}
		return classes;
	}

	public IPackage getPackage() {
		return pkg;
	}

	public File getFile() {
		return file;
	}
	
	public String getRelativePath() {
		return this.relativePath;
	}

	public String getSource() {
		return source;
	}

}

