/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.Attribute;

import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;

public class AttributeUse extends Use {

	public AttributeUse(Attribute node, Module module) {
		super(NodeUtils.getId(node.attr), node, module);
	}

}
