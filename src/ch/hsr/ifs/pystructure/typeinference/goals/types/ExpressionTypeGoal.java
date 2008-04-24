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

import org.python.pydev.parser.jython.SimpleNode;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.ILocatable;
import ch.hsr.ifs.pystructure.typeinference.goals.base.Location;
import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;

public class ExpressionTypeGoal extends AbstractTypeGoal implements ILocatable {

	private final SimpleNode expression;
	
	public ExpressionTypeGoal(ModuleContext context, SimpleNode expression) {
		super(context);
		if (expression == null) {
			throw new IllegalArgumentException("expression may not be null");
		}
		this.expression = expression;
	}

	public SimpleNode getExpression() {
		return expression;
	}
	
	public Location getLocation() {
		return new Location(context, expression);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ExpressionTypeGoal) {
			ExpressionTypeGoal goal = (ExpressionTypeGoal) obj;
			return expression == goal.expression;
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (expression != null) {
			return expression.hashCode();
		}
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "ExpressionTypeGoal: "
				+ expression
				+ NodeUtils.nodePosition(expression)
				+ " context: " + context;
	}
}
