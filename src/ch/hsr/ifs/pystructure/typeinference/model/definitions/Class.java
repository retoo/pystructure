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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.exprType;

/**
 * Definition of a class.
 */
public class Class extends StructureDefinition {

	private final List<Method> methods;
	private final Map<String, Attribute> attributes;
	private final List<exprType> baseClasses;
	private final Set<Class> subClasses;
	private MethodResolutionOrder linearization;

	public Class(String name, ClassDef classDef, Module module) {
		super(module, name, classDef);
		this.methods = new ArrayList<Method>();
		this.baseClasses = Arrays.asList(classDef.bases);
		
		this.attributes = new HashMap<String, Attribute>();
		subClasses = new HashSet<Class>();
	}
	
	public void addMethod(Method method) {
		methods.add(method);
	}

	public Method getMethod(String name) {
		for (Method method : methods) {
			if (method.getName().equals(name)) {
				return method;
			}
		}
		return null;
	}
	
	public void addAttribute(Attribute attribute) {
		attributes.put(attribute.getName(), attribute);
	}
	
	public Attribute getAttribute(String name) {
		return attributes.get(name);
	}
	
	public List<Method> getMethods() {
		return methods;
	}

	@Override
	public String getDescription() {
		return "class '" + getName() + "'";
	}
	
	public List<exprType> getBaseClasses() {
		return baseClasses;
	}

	public Map<String, Attribute> getAttributes() {
		return attributes;
	}
	
	public String getFullName() {
		return getModule().getNamePath().toString() + "." + getName();
	}

	public MethodResolutionOrder getLinearization() {
		return linearization;
	}

	public void setLinearization(MethodResolutionOrder linearization) {
		this.linearization = linearization;
	}

	public void addSubClass(Class klass) {
		subClasses.add(klass);
	}
	
	public Set<Class> getSubClasses() {
		return subClasses;
	}
	
	@Override
	public String toString() {
		return "Class " + getName() + " " + getNodePosition();
	}

}
