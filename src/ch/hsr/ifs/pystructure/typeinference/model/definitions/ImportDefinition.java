/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import ch.hsr.ifs.pystructure.typeinference.model.base.IModule;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

public class ImportDefinition extends Definition implements IAttributeDefinition {

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
	
	@Override
	public String toString() {
		return "Import of " + element + " aliased as " + getName();
	}

}
