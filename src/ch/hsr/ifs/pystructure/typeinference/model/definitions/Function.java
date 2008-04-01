/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.FunctionDef;
import org.python.pydev.parser.jython.ast.exprType;

/**
 * Definition of a function.
 */
public class Function extends StructureDefinition implements IAttributeDefinition {

	private final Definition attributeParent;

	public Function(Module module, String name, FunctionDef functionDef, Definition attributeParent) {
		super(module, name, functionDef);
		assert attributeParent != null;
		this.attributeParent = attributeParent;
	}

	public boolean isFirstArgument(Argument argument) {
		exprType firstArgument = getNode().args.args[0];
		return firstArgument.equals(argument.getNode());
	}
	
	@Override
	public FunctionDef getNode() {
		return (FunctionDef) super.getNode();
	}

	public Definition getAttributeParent() {
		return attributeParent;
	}

	public String toString() {
		return "Function " + getName() + " " + getNodePosition();
	}

}
