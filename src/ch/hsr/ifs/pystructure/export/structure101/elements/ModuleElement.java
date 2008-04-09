/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.export.structure101.elements;

import org.jdom.Element;

public class ModuleElement extends Element {

	private static final long serialVersionUID = 1L;
	
	public ModuleElement(String name, String id) {
		super("module");
		
		this.setAttribute("type", "module");
		this.setAttribute("name", name);
		this.setAttribute("id", id);
	}

}
