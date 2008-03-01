/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.definitions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ParseException;
import org.python.pydev.refactoring.ast.visitors.VisitorFactory;
import org.python.pydev.refactoring.typeinference.model.base.IModule;
import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;
import org.python.pydev.refactoring.utils.FileUtils;

public class Module extends Definition<org.python.pydev.parser.jython.ast.Module> implements PathElement, IModule {

	private String path;
	private ArrayList<Use> containedUses;
	private ArrayList<Definition> definitions;
	private IPackage pkg;
	private File file;
	private String relativePath;

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
	
	public Module(String modulename, String source, IPackage pkg) {
		/* create a fake file */
		this.file = new File(modulename);
		this.path = "created for test as " + modulename;
		this.relativePath = file.getPath();
		
		init(pkg, source, modulename);
	}

	private void init(IPackage pkg, String source, String name) {
		this.pkg = pkg;

		this.containedUses = new ArrayList<Use>();
		this.definitions  = new ArrayList<Definition>();

		org.python.pydev.parser.jython.ast.Module module;
		try {
			module = VisitorFactory.parse(source);
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

	public IPackage getPackage() {
		return pkg;
	}

	public File getFile() {
		return file;
	}
	
	public String getRelativePath() {
		return this.relativePath;
	}

}

