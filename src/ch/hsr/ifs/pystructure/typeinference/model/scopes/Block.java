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

package ch.hsr.ifs.pystructure.typeinference.model.scopes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;

/**
 * Abstraction of a block of Python code (method body, if body, basically
 * everything with indentation). It knows about its parent block and is used to
 * store all definitions which are made inside it.
 */
public class Block {

	private final Block parent;
	
	/**
	 * Contains all the definitions ever made in this block, even such which are
	 * overwritten later.
	 */
	private final List<Definition> blockDefinitions;

	/**
	 * Contains the currently active definitions for a given name (changes as
	 * the processing code walks through the block and finds new definitions).
	 */
	private final Map<String, List<Definition>> currentDefinitions;

	public Block(Block parent) {
		this.parent = parent;
		this.blockDefinitions = new LinkedList<Definition>();
		this.currentDefinitions = new HashMap<String, List<Definition>>();
	}
	
	public void setDefinition(Definition definition) {
		List<Definition> definitions = new LinkedList<Definition>();
		definitions.add(definition);
		currentDefinitions.put(definition.getName(), definitions);
		blockDefinitions.add(definition);
	}
	
	public void addDefinition(Definition definition) {
		String name = definition.getName();
		List<Definition> definitions = currentDefinitions.get(name);
		if (definitions != null) {
			definitions.add(definition);
			blockDefinitions.add(definition);
		} else {
			setDefinition(definition);
		}
	}
	
	public void addBlockDefinitions(Block other) {
		for (Definition definition : other.blockDefinitions) {
			addDefinition(definition);
		}
	}
	
	public void addCurrentDefinitions(Block other) {
		for (Definition definition : other.getCurrentDefinitions()) {
			addDefinition(definition);
		}
	}
	
	public List<Definition> getCurrentDefinitions() {
		List<Definition> definitions = new LinkedList<Definition>();
		for (List<Definition> d : currentDefinitions.values()) {
			definitions.addAll(d);
		}
		return definitions;
	}
		
	public List<Definition> getCurrentDefinitions(String name) {
		List<Definition> definitions = currentDefinitions.get(name);
		if (definitions != null) {
			return definitions;
		} else {
			return getParentDefinitions(name);
		}
	}
	
	protected List<Definition> getParentDefinitions(String name) {
		return getParent().getCurrentDefinitions(name);
	}
	
	protected List<Definition> getAllDefinitions(String name) {
		List<Definition> definitions = new LinkedList<Definition>();
		for (Definition definition : blockDefinitions) {
			if (name.equals(definition.getName())) {
				definitions.add(definition);
			}
		}
		
		// If there hasn't been a single definition of name in the block, get
		// the definitions in the parent block (may be recursive)
		if (definitions.isEmpty()) {
			definitions.addAll(getParent().getAllDefinitions(name));
		}
		return definitions;
	}
	
	public Block getParent() {
		return parent;
	}

}
