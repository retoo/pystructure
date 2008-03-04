/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.scopes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;

public class Block {
	private final Block parent;
	
	private List<Definition> allDefinitions;
	private Map<NameAdapter, List<Definition>> currentDefinitions;

	public Block(Block parent) {
		this.parent = parent;
		this.allDefinitions = new ArrayList<Definition>();
		this.currentDefinitions = new TreeMap<NameAdapter, List<Definition>>();
	}
	
	public void addToCurrentDefinitions(Definition definition) {
		NameAdapter name = definition.getName();
		List<Definition> definitions = currentDefinitions.get(name);
		if (definitions != null) {
			definitions.add(definition);
			allDefinitions.add(definition);
		} else {
			setCurrentDefinition(definition);
		}
	}
	
	public void addToCurrentDefinitions(List<Definition> definitions) {
		for (Definition definition : definitions) {
			addToCurrentDefinitions(definition);
		}
	}
	
	public List<Definition> getCurrentBlockDefinitions() {
		List<Definition> definitions = new ArrayList<Definition>();
		for (List<Definition> d : currentDefinitions.values()) {
			definitions.addAll(d);
		}
		return definitions;
	}
	
	public List<Definition> getBlockDefinitions() {
		return allDefinitions;
	}
	
	public void setCurrentDefinition(Definition definition) {
		List<Definition> definitions = new ArrayList<Definition>();
		definitions.add(definition);
		currentDefinitions.put(definition.getName(), definitions);
		allDefinitions.add(definition);
	}
	
	public void setCurrentDefinition(List<Definition> definitions) {
		for (Definition definition : definitions) {
			setCurrentDefinition(definition);
		}
	}
	
	public List<Definition> getDefinitions(NameAdapter name) {
		List<Definition> definitions = currentDefinitions.get(name);
		if (definitions != null) {
			return definitions;
		} else {
			return getParentDefinitions(name);
		}
	}
	
	protected List<Definition> getParentDefinitions(NameAdapter name) {
		return getParent().getDefinitions(name);
	}
	
	protected List<Definition> getAllDefinitions(NameAdapter name) {
		List<Definition> definitions = new ArrayList<Definition>();
		for (Definition definition : allDefinitions) {
			if (name.equals(definition.getName())) {
				definitions.add(definition);
			}
		}
		if (definitions.isEmpty()) {
			definitions.addAll(getParent().getAllDefinitions(name));
		}
		return definitions;
	}
	
	public Block getParent() {
		return parent;
	}
}
