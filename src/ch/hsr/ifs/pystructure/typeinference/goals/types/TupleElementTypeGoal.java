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
import ch.hsr.ifs.pystructure.typeinference.model.definitions.TupleElement;

public class TupleElementTypeGoal extends AbstractTypeGoal {

	private final TupleElement element;
	
	public TupleElementTypeGoal(ModuleContext context, TupleElement element) {
		super(context);
		this.element = element;
	}
	
	public TupleElement getTupleElement() {
		return element;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof TupleElementTypeGoal) {
			TupleElementTypeGoal goal = (TupleElementTypeGoal) obj;
			return element == goal.element;
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (element != null) {
			return element.hashCode();
		}
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "TupleElementTypeGoal: "
				+ ((element != null) ? element.toString() : "null")
				+ " context: "
				+ ((context != null) ? context.toString() : "null");
	}
}
