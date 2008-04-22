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

package ch.hsr.ifs.pystructure.typeinference.goals.references;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.AbstractGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Attribute;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;

public class AttributeReferencesGoal extends AbstractGoal {

	private final Attribute attribute;
	private final ClassType classType;

	public final List<AttributeReference> references;

	public AttributeReferencesGoal(ModuleContext context, Attribute attribute,
			ClassType classType) {
		super(context);
		this.attribute = attribute;
		this.classType = classType;
		
		this.references = new ArrayList<AttributeReference>();
	}

	public Attribute getAttribute() {
		return attribute;
	}
	
	public ClassType getClassType() {
		return classType;
	}

	public String toString() {
		return "AttributeReferencesGoal: "
		+ "Attribute " + attribute
		+ " context: " + context;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result
				+ ((classType == null) ? 0 : classType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AttributeReferencesGoal other = (AttributeReferencesGoal) obj;
		if (attribute == null) {
			if (other.attribute != null) {
				return false;
			}
		} else if (!attribute.equals(other.attribute)) {
			return false;
		}
		if (classType == null) {
			if (other.classType != null) {
				return false;
			}
		} else if (!classType.equals(other.classType)) {
			return false;
		}
		return true;
	}

}
