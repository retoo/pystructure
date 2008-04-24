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

package ch.hsr.ifs.pystructure.typeinference.goals.types;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.ILocatable;
import ch.hsr.ifs.pystructure.typeinference.goals.base.Location;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;

public class DefinitionTypeGoal extends AbstractTypeGoal implements ILocatable {

	private final Definition definition;

	public DefinitionTypeGoal(ModuleContext context, Definition definition) {
		super(context);
		if (definition == null) {
			throw new IllegalArgumentException("definition may not be null");
		}
		this.definition = definition;
	}

	public Definition getDefinition() {
		return definition;
	}
	
	public Location getLocation() {
		return new Location(context, definition);
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof DefinitionTypeGoal) {
			DefinitionTypeGoal goal = (DefinitionTypeGoal) obj;
			return definition == goal.definition;
		}
		return false;
	}

	public int hashCode() {
		if (definition != null) {
			return definition.hashCode();
		}
		return super.hashCode();
	}

	public String toString() {
		return "DefinitionTypeGoal: "
		+ ((definition != null) ? definition.toString() : "null")
		+ " context: " + context;
	}
}
