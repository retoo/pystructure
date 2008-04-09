/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.references;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.AbstractGoal;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Attribute;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;

public class AttributeReferencesGoal extends AbstractGoal {

	public final List<AttributeReference> references;
	private Attribute attribute;

	public AttributeReferencesGoal(ModuleContext context,
			Attribute attribute) {
		super(context);
		this.attribute = attribute;
		this.references = new ArrayList<AttributeReference>();
	}

	public Attribute getAttribute() {
		return attribute;
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
				+ ((references == null) ? 0 : references.hashCode());
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
		if (references == null) {
			if (other.references != null) {
				return false;
			}
		} else if (!references.equals(other.references)) {
			return false;
		}
		return true;
	}

}
