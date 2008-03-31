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
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;

public class AttributeReferencesGoal extends AbstractGoal {

	private final String attributeName;
	private final Definition attributeParent;
	public final List<AttributeReference> references;

	public AttributeReferencesGoal(ModuleContext context,
			String attributeName, Definition attributeParent) {
		super(context);
		this.attributeName = attributeName;
		this.attributeParent = attributeParent;
		this.references = new ArrayList<AttributeReference>();
	}

	public String getAttributeName() {
		return attributeName;
	}

	public Definition getAttributeParent() {
		return attributeParent;
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
		if (attributeName == null) {
			if (other.attributeName != null) {
				return false;
			}
		} else if (!attributeName.equals(other.attributeName)) {
			return false;
		}
		if (attributeParent == null) {
			if (other.attributeParent != null) {
				return false;
			}
		} else if (!attributeParent.equals(other.attributeParent)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributeName == null) ? 0 : attributeName.hashCode());
		result = prime * result
				+ ((attributeParent == null) ? 0 : attributeParent.hashCode());
		return result;
	}

	public String toString() {
		return "AttributeReferencesGoal: "
		+ "Attribute " + attributeName
		+ " of " + attributeParent
		+ " context: " + context;
	}

}
