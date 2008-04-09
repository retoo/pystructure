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
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;
import ch.hsr.ifs.pystructure.typeinference.results.references.ClassReference;

// TODO: Maybe implement with an AttributeReferencesGoal (with Module as the parent)?
public class ClassReferencesGoal extends AbstractGoal {

	private final Class klass;
	public final List<ClassReference> references;

	public ClassReferencesGoal(ModuleContext context, Class klass) {
		super(context);
		this.klass = klass;
		this.references = new ArrayList<ClassReference>();
	}
	
	public Class getKlass() {
		return klass;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ClassReferencesGoal) {
			ClassReferencesGoal goal = (ClassReferencesGoal) obj;
			return klass == goal.klass;
		}
		return false;
	}

	public int hashCode() {
		if (klass != null) {
			return klass.hashCode();
		}
		return super.hashCode();
	}

	public String toString() {
		return "ClassReferencesGoal: "
		+ ((klass != null) ? klass.toString() : "null")
		+ " context: "
		+ ((getContext() != null) ? getContext().toString() : "null");
	}
}
