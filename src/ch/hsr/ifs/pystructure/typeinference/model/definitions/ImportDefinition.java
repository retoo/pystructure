/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;

/* TODO: as ordered by robin, there used to be a implements Iattribute something which is now removed */
public class ImportDefinition extends Definition {

	private final NamePath path;
	private final String element;
	private final int level;

	public ImportDefinition(Module module, SimpleNode node, NamePath path, String element, String alias) {
		super(module, alias, node);
		this.path = path;
		this.element = element;
		this.level = 0;
	}
	
	public ImportDefinition(Module module, SimpleNode node, NamePath path, String element, String alias, int level) {
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
	
}
