/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.definitions;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.refactoring.typeinference.model.base.IModule;
import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;

public class ImportDefinition extends Definition<SimpleNode> implements IAttributeDefinition {

	private Definition parent;
	private Object element;

	public ImportDefinition(IModule module, NameAdapter alias, Object element, Definition parent) {
		this(module, alias, element);
		this.parent = parent;
	}
	
	public ImportDefinition(IModule module, NameAdapter alias, Object element) {
		super(module, alias, null);
		this.element = element;
	}

	public Object getElement() {
		return element;
	}

	public Definition getAttributeParent() {
		return parent;
	}

}
