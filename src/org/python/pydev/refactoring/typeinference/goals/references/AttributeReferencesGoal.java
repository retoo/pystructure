/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.goals.references;

import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.goals.base.PythonGoal;
import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;
import org.python.pydev.refactoring.typeinference.model.definitions.Definition;

public class AttributeReferencesGoal extends PythonGoal {

	private final NameAdapter attributeName;
	private final Definition attributeParent;

	public AttributeReferencesGoal(PythonContext context,
			NameAdapter attributeName, Definition attributeParent) {
		super(context);
		this.attributeName = attributeName;
		this.attributeParent = attributeParent;
	}

	public NameAdapter getAttributeName() {
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
		+ ((attributeName != null) ? attributeName.toString() : "null")
		+ ((attributeParent != null) ? attributeParent.toString() : "null")
		+ " context: "
		+ ((getContext() != null) ? getContext().toString() : "null");
	}

}
