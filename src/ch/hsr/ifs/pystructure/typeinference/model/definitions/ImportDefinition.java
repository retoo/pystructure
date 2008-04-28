/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import org.python.pydev.parser.jython.SimpleNode;

/**
 * Definition which happens because of an import, for example:
 * 
 * import pkg.module  # results in ImportDefinition for pkg
 * from pkg.module import Class  # results in ImportDefinition for Class
 */
public class ImportDefinition extends Definition {

	private final ImportPath path;
	private final String element;

	public ImportDefinition(Module module, SimpleNode node, ImportPath path, String element, String alias) {
		super(module, alias, node);
		this.path = path;
		this.element = element;
	}
	
	public ImportPath getPath() {
		return path;
	}
	
	public String getElement() {
		return element;
	}
	
	@Override
	public String toString() {
		return "import of " + path + (element != null ? "." + element : ""); 
	}

}
