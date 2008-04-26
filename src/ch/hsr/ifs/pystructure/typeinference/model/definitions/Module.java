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

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.ParseException;

import ch.hsr.ifs.pystructure.parser.Parser;
import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;
import ch.hsr.ifs.pystructure.utils.FileUtils;

public class Module extends StructureDefinition implements PathElement {

	private final NamePath namePath;
	private final PathElementContainer parent;
	private final File file;
	
	private ArrayList<Use> containedUses;
	private ArrayList<Definition> definitions;

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

	@Override
	public String toString() {
		return namePath.toString();
	}

}

