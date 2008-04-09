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

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.model.base.NamePath;

public class Attribute extends Definition {
	
	private final String name;
	private final NamePath namePath;
	private final Class klass;
	public List<? extends Reference> references;

	public Attribute(String name, Class klass) {
		super();
		this.name = name;
		this.namePath = new NamePath(new NamePath(klass.getFullName()), name);
		this.klass = klass;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public Class getKlass() {
		return klass;
	}
	
	public NamePath getNamePath() {
		return namePath;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((klass == null) ? 0 : klass.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Attribute other = (Attribute) obj;
		if (klass == null) {
			if (other.klass != null) {
				return false;
			}
		} else if (!klass.equals(other.klass)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}


}
