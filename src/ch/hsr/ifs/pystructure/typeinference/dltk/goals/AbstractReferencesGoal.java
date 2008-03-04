/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.dltk.goals;

import ch.hsr.ifs.pystructure.typeinference.dltk.contexts.IContext;

public class AbstractReferencesGoal extends AbstractGoal {

	private final String name;
	private final String parentModelKey;

	public AbstractReferencesGoal(IContext context, String name,
			String parentKey) {
		super(context);
		this.name = name;
		parentModelKey = parentKey;
	}

	public String getName() {
		return name;
	}

	public String getParentModelKey() {
		return parentModelKey;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((parentModelKey == null) ? 0 : parentModelKey.hashCode());
		return result;
	}

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
		final AbstractReferencesGoal other = (AbstractReferencesGoal) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (parentModelKey == null) {
			if (other.parentModelKey != null) {
				return false;
			}
		} else if (!parentModelKey.equals(other.parentModelKey)) {
			return false;
		}
		return true;
	}

}
