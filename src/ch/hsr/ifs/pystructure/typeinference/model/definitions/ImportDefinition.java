/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.model.base.IModule;
import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;

public class ImportDefinition extends Definition implements IAttributeDefinition {

	private final NamePath path;
	private final String element;
	private final int level;

	public ImportDefinition(IModule module, SimpleNode node, NamePath path, String element, String alias) {
		super(module, alias, node);
		this.path = path;
		this.element = element;
		this.level = 0;
	}
	
	public ImportDefinition(IModule module, SimpleNode node, NamePath path, String element, String alias, int level) {
		super(module, alias, node);
		this.path = path;
		this.element = element;
		this.level = level;
	}

	public NamePath getPath() {
		return path;
	}
	
	public String getElement() {
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
