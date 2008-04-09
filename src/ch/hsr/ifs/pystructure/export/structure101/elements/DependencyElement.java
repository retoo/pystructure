/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.export.structure101.elements;

import org.jdom.Element;

import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.StructureDefinition;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.FunctionType;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.results.types.ModuleType;
import ch.hsr.ifs.pystructure.typeinference.results.types.TupleType;

public class DependencyElement extends Element {

	private static final long serialVersionUID = 1L;
	
	private boolean valid;

	private DependencyElement() {
		super("dependency");
	
		this.valid = true;
		
		this.setAttribute("type", "references");
	}
	
	public DependencyElement(String from, IType type) {
		this();
		this.setFrom(from);
		this.setTo(type);
	}

	public DependencyElement(StructureDefinition from, IType to) {
		this();
		
		this.setFrom(from);
		this.setTo(to);
	}
	
	public DependencyElement(StructureDefinition from, Definition to) {
		this();
		
		this.setFrom(from);
		this.setTo(to);
	}

	private void setTo(IType type) {
		setTo(getTypeIdentifier(type));
	}
	
	private void setTo(Definition to) {
		if (to == null) {
			this.valid = false;
		} else {
			this.setAttribute("to", to.getUniqueIdentifier());
		}
	}

	private void setFrom(StructureDefinition definition) {
		this.setFrom(definition.getUniqueIdentifier());
	}
	
	private void setFrom(String from) {
		this.setAttribute("from", from);
	}

	private StructureDefinition  getTypeIdentifier(IType type) {
		if (type instanceof ClassType) {
			ClassType classType = (ClassType) type;
			return classType.getKlass();
		} else if (type instanceof MetaclassType) {
			MetaclassType classType = (MetaclassType) type;
			return classType.getKlass();
		} else if (type instanceof FunctionType) {
			FunctionType functionType = (FunctionType) type;
			return functionType.getFunction();
		} else if (type instanceof ModuleType) {
			ModuleType moduleType = (ModuleType) type;
			return moduleType.getModule();
		} else if (type instanceof TupleType) {
//			TupleType tupleType = (TupleType) type;
			return null;
		} else {
			throw new RuntimeException("Unknown type" + type);
		}
	}

	public boolean isValid() {
		return valid;
	}

}
