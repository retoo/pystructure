/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
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

package ch.hsr.ifs.pystructure.typeinference.results.types;

import org.python.pydev.parser.jython.ast.Call;

import ch.hsr.ifs.pystructure.typeinference.model.base.NodeUtils;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Class;

public class ClassType extends AbstractType {

	private final Class klass;
	private final Call constructorCall;
	
	public ClassType(String typeName) {
		super(typeName);
		this.klass = null;
		this.constructorCall = null;
		/* FIXME: shouldn't we use a different class for that? */
	}

	public ClassType(Class klass, Call constructorCall) {
		super(klass.getName());
		this.klass = klass;
		this.constructorCall = constructorCall;
	}
	
	public Class getKlass() {
		return klass;
	}
	
	@Override
	public String toString() {
		if (klass == null) {
			return "type " + typeName;
		} else {
			return "type " + klass + " constructed at " + NodeUtils.nodePosition(constructorCall);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constructorCall == null) ? 0 : constructorCall.hashCode());
		result = prime * result + ((klass == null) ? 0 : klass.hashCode());
		result = prime * result
				+ ((typeName == null) ? 0 : typeName.hashCode());
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
		final ClassType other = (ClassType) obj;
		if (constructorCall == null) {
			if (other.constructorCall != null) {
				return false;
			}
		} else if (!constructorCall.equals(other.constructorCall)) {
			return false;
		}
		if (klass == null) {
			if (other.klass != null) {
				return false;
			}
		} else if (!klass.equals(other.klass)) {
			return false;
		}
		if (typeName == null) {
			if (other.typeName != null) {
				return false;
			}
		} else if (!typeName.equals(other.typeName)) {
			return false;
		}
		return true;
	}

}
