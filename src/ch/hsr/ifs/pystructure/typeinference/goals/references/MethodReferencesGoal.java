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

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Method;

public class MethodReferencesGoal extends CallableGoal {

	private final Method method;

	public MethodReferencesGoal(ModuleContext context, Method method) {
		super(context);
		this.method = method;
	}
	
	public Method getMethod() {
		return method;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof MethodReferencesGoal) {
			MethodReferencesGoal goal = (MethodReferencesGoal) obj;
			return method == goal.method;
		}
		return false;
	}

	public int hashCode() {
		if (method != null) {
			return method.hashCode();
		}
		return super.hashCode();
	}

	public String toString() {
		return "MethodReferencesGoal: "
		+ ((method != null) ? method.toString() : "null")
		+ " context: "
		+ ((getContext() != null) ? getContext().toString() : "null");
	}
}
