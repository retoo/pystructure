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
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

/**
 * Definition of a class.
 */
public class Class extends Definition<ClassDef> implements IAttributeDefinition {

	private List<Method> methods;
	private Module module;
	public HashMap<NameAdapter, CombinedType> attributes;

	public Class(NameAdapter name, ClassDef classDef, Module module) {
		super(module, name, classDef);
		this.methods = new ArrayList<Method>();
		this.module = module;
		this.attributes = new HashMap<NameAdapter, CombinedType>();
	}
	
	public void addMethod(Method method) {
		methods.add(method);
	}
	
	public Method getMethodNamed(NameAdapter name) {
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

}
