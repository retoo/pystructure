/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.model.base.IModule;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

public class ImportDefinition extends Definition implements IAttributeDefinition {

	private final NameAdapter path;
	private final NameAdapter element;
	private final int level;

	public ImportDefinition(IModule module, SimpleNode node, NameAdapter path, NameAdapter element, NameAdapter alias) {
		super(module, alias, node);
		this.path = path;
		this.element = element;
		this.level = 0;
	}
	
	public ImportDefinition(IModule module, SimpleNode node, NameAdapter path, NameAdapter element, NameAdapter alias, int level) {
		super(module, alias, node);
		this.path = path;
		this.element = element;
		this.level = level;
	}

	public NameAdapter getPath() {
		return path;
	}
	
	public NameAdapter getElement() {
		return element;
	}
	
	public int getLevel() {
		return level;
	}
	
	public Definition getAttributeParent() {
		// TODO Auto-generated method stub
		return null;
	}

}
