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
import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;
import ch.hsr.ifs.pystructure.utils.FileUtils;

public class Module extends StructureDefinition implements PathElement, IModule {

	private final NamePath namePath;
	private final PathElementContainer parent;
	private final File file;
	
	private ArrayList<Use> containedUses;
	private ArrayList<Definition> definitions;
	private String source;

	public Module(String name, PathElementContainer parent, File file) {
		this.namePath = new NamePath(parent.getNamePath(), name);
		this.parent = parent;
		this.file = file;
		
		try {
			String source = FileUtils.read(file);
			init(source, name);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void init(String source, String name) {
		this.containedUses = new ArrayList<Use>();
		this.definitions  = new ArrayList<Definition>();
		this.source = source;

		org.python.pydev.parser.jython.ast.Module module;
		try {
			module = Parser.parse(source);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse module " + this, e);
		}
		
		super.init(this, name, module);
	}

	public NamePath getNamePath() {
		return namePath;
	}
	
	public PathElementContainer getParent() {
		return parent;
	}
	
	// TODO: Should return all possible definitions, not just one.

	public Definition getChild(String name) {
		for (Definition definition : definitions) {
			if (definition.getName().equals(name)) {
				return definition;
			}
		}
		
		return new NoDefintion(this, name);
//		throw new RuntimeException("Object " + name + " not defined in " + this);
	}

	@Override
	public String toString() {
		return namePath.toString();
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

	public File getFile() {
		return file;
	}
	
	@Override
	public String getUniqueIdentifier() {
		return getNamePath().toString();
	}

	public String getSource() {
		return source;
	}

}

