/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.definitions;

import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;

public class AttributeUse extends Use {

	public AttributeUse(Attribute node, Module module) {
		super(new NameAdapter(node.attr), node, module);
	}

}
