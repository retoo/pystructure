/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

/**
 * A use of a name, could also be called reference. It can have multiple
 * definitions.
 */
public class NameUse extends Use {
	
	private List<Definition> definitions;
	
	public NameUse(NameAdapter name, Module module) {
		super(name, name.getNode(), module);
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

	public List<NameUse> getRelated() {
		/* Set with all nameUses in it */
		Set<NameUse> nameUses = new HashSet<NameUse>();
		/* Set of definitions we haven already seen */
		Set<Definition> seenDefintions = new HashSet<Definition>(definitions);
		/* Queue of definitions we still have to traverse */
		Queue<Definition> toDoDefintions = new LinkedList<Definition>(definitions);
		
		while (!toDoDefintions.isEmpty()) {
			Definition definition = toDoDefintions.poll();
			
			List<NameUse> uses = definition.getUses();
			for (NameUse nameUse : uses) {
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
		
		return new LinkedList(nameUses);
	}
	
	public String toString() {
		SimpleNode node = getName().getNode();
		return getName() + " (L" + node.beginLine + "C" + node.beginColumn +")";
	}
}
