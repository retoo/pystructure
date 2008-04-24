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

package ch.hsr.ifs.pystructure.typeinference.contexts;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.results.types.ClassType;

/**
 * Context of a class instance. This is necessary for the following case:
 * 
 * <code>
 * class Container(object):
 *     def set(self, element):
 *         self.element = element
 *   
 *     def get(self):
 *         return self.element
 * 
 * c1 = Container()
 * c1.set(3.14)
 * c1.get() ## type float
 * 
 * c2 = Container()
 * c2.set(42)
 * c2.get() ## type str
 * </code>
 * 
 * The instance context is necessary so that the ExpressionTypeGoal for the
 * "element" argument of the "set" method only considers the one instance.
 * 
 * Without the context, both results would be float|int.
 */
public class InstanceContext extends ModuleContext {

	private final ClassType classType;
	
	public InstanceContext(ModuleContext parent, Module module, ClassType classType) {
		super(parent, module);
		this.classType = classType;
	}

	public ClassType getClassType() {
		return classType;
	}
	
	@Override
	public String toString() {
		return "InstanceContext " + classType;
	}
	
}
