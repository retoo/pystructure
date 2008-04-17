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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.python.pydev.parser.jython.ast.exprType;

import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;

/**
 * A use of a name, could also be called reference. It can have multiple
 * definitions.
 */
public class NameUse extends Use {
	
	private List<Definition> definitions;
	
	public NameUse(String name, exprType expression, Module module) {
		super(name, expression, module);
		this.definitions = new ArrayList<Definition>();
	}
	
	public List<Definition> getDefinitions() {
		return definitions;
	}
	
	public void addDefinition(Definition definition) {
		definitions.add(definition);
		definition.addUse(this);
	}
	
	public void addDefinitions(List<Definition> definitions) {
		for (Definition d : definitions) {
			addDefinition(d);
		}
	}

	/* Currently not being used, but was used in PEPTIC, please recheck if 
	 * method still does what should do, and think about removing deprecated */
	@Deprecated
	public List<NameUse> getRelated() {
		/* Read comment */
		/* Really, read comment above */
		
		/* Set with all nameUses in it */
		Set<NameUse> nameUses = new HashSet<NameUse>();
		/* Set of definitions we haven already seen */
		Set<Definition> seenDefintions = new HashSet<Definition>(definitions);
		/* Queue of definitions we still have to traverse */
		Queue<Definition> toDoDefintions = new LinkedList<Definition>(definitions);
		
		while (!toDoDefintions.isEmpty()) {
			Definition definition = toDoDefintions.poll();
			
			for (NameUse nameUse : definition.getUses()) {
				/* Have we already seen this nameUse? */
				if (!nameUses.contains(nameUse)) {
					/* IF not we register it in our set */
					nameUses.add(nameUse);
					/* And we have to check all definitions related to this nameUse */
					for (Definition newDef : nameUse.definitions) {
						if (!seenDefintions.contains(newDef)) {
							toDoDefintions.addAll(nameUse.getDefinitions());
							seenDefintions.add(newDef);
						}
					}
				}
			}
		}
		
		return new LinkedList<NameUse>(nameUses);
	}
	
	public String toString() {
		return getName() + " " + NodeUtils.nodePosition(getExpression());
	}
}
