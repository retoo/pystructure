/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.python.pydev.parser.jython.ast.ClassDef;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;

/**
 * Definition of a class.
 */
public class Class extends StructureDefinition implements IAttributeDefinition {

	private List<Method> methods;
	private Module module;
	public HashMap<String, CombinedType> attributes;

	public Class(String name, ClassDef classDef, Module module) {
		super(module, name, classDef);
		this.methods = new ArrayList<Method>();
		this.module = module;
		this.attributes = new HashMap<String, CombinedType>();
	}
	
	public void addMethod(Method method) {
		methods.add(method);
	}
	
	public Method getMethodNamed(String name) {
		for (Method method : methods) {
			if (method.getName().equals(name)) {
				return method;
			}
		}
		return null;
	}
	
	public List<Method> getMethods() {
		return methods;
	}

	
	@Override
	public String getDescription() {
		return "class '" + getName() + "'";
	}
	
	@Override
	public String toString() {
		return "Class " + getName() + " " + getNodePosition();
	}

	public Module getModule() {
		return module;
	}
	
	public Definition getAttributeParent() {
		return module;
	}

	public HashMap<String, CombinedType> getAttributes() {
		return attributes;
	}
	
	public String getFullName() {
		return getModule().getNamePath().toString() + "." + getName();
	}

}
