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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;

/*
 * FIXME: robin: extending from structure definition AND implementing
 * pathelementcontainer feels a bit wrong, can we dot that here?
 */ 
public class Package extends StructureDefinition implements PathElement, PathElementContainer {
	
	private final String name;
	private final NamePath namePath;
	private final PathElementContainer parent;
	
	private Map<String, PathElement> children;

	public Package(String name, PathElementContainer parent, File dir, File initFile) {
		this.name = name;
		this.namePath = new NamePath(parent.getNamePath(), name);
		this.parent = parent;
		this.children = new HashMap<String, PathElement>();
	}
	
	public String getName() {
		return name;
	}
	
	public NamePath getNamePath() {
		return namePath;
	}
	
	public void addChild(PathElement child) {
		children.put(child.getName(), child);
	}
	
	public PathElement getChild(String name) {
		return children.get(name);
	}
	
	public PathElementContainer getParent() {
		return parent;
	}
	
}
