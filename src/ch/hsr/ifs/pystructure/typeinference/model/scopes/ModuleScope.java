/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.scopes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.NameUse;

public class ModuleScope extends Scope {

	private Map<String, List<Definition>> globalDefinitions;
	private Map<String, List<NameUse>> globalNameUses;
	
	public ModuleScope(Block parent, Module module) {
		super(parent, module);
		globalDefinitions = new HashMap<String, List<Definition>>();
		globalNameUses = new HashMap<String, List<NameUse>>();
	}
	
	public void addGlobalDefinition(Definition definition) {
		List<Definition> definitions = globalDefinitions.get(definition.getName());
		if (definitions == null) {
			definitions = new ArrayList<Definition>();
			globalDefinitions.put(definition.getName(), definitions);
		}
		definitions.add(definition);
	}

	public void addGlobalNameUse(NameUse use) {
		List<NameUse> nameUses = globalNameUses.get(use.getName());
		if (nameUses == null) {
			nameUses = new ArrayList<NameUse>();
			globalNameUses.put(use.getName(), nameUses);
		}
		nameUses.add(use);
	}
	
	public void connectGlobals() {
		for (Entry<String, List<NameUse>> entry : globalNameUses.entrySet()) {
			String name = entry.getKey();
			List<NameUse> nameUses = entry.getValue();
			for (NameUse nameUse : nameUses) {
				List<Definition> definitions = globalDefinitions.get(name);
				if (definitions != null) {
					nameUse.addDefinitions(definitions);
				}
			}
		}
	}
}
