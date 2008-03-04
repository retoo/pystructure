/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.FunctionDef;
import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.model.base.IModule;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

/**
 * Definition of a function.
 */
public class Function extends Definition<FunctionDef> implements IAttributeDefinition {

	private final Definition attributeParent;

	public Function(IModule module, NameAdapter name, FunctionDef functionDef, Definition attributeParent) {
		super(module, name, functionDef);
		this.attributeParent = attributeParent;
	}

	public boolean isFirstArgument(Argument argument) {
		exprType firstArgument = getNode().args.args[0];
		return firstArgument.equals(argument.getName().getNode());
	}

	public Definition getAttributeParent() {
		return attributeParent;
	}

	public String toString() {
		return "Function " + getName() + " defined at line " + getNode().beginLine + ", column " + getNode().beginColumn;
	}

}
