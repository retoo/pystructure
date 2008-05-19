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

package ch.hsr.ifs.pystructure.typeinference.basetype;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.hsr.ifs.pystructure.utils.StringUtils;

public class CombinedType implements IType, Iterable<IType> {

	private Set<IType> types;
	
	public CombinedType() {
		types = new LinkedHashSet<IType>();
	}
	
	public void appendType(IType type) {
		if (type instanceof CombinedType) {
			CombinedType combinedType = (CombinedType) type;
			types.addAll(combinedType.types);
		} else {
			types.add(type);
		}
	}
	
	public void appendType(List<? extends IType> types) {
		for (IType type : types) {
			appendType(type);
		}
	}
	
	public Set<IType> getTypes() {
		return types;
	}
	
	public String getTypeName() {
		SortedSet<String> set = new TreeSet<String>();
		for (IType type : types) {
			set.add(type.getTypeName());
		}
		return StringUtils.join('|', set);
	}

	@Override
	public String toString() {
		SortedSet<String> set = new TreeSet<String>();
		for (IType type : types) {
			set.add(type.toString());
		}
		return StringUtils.join('|', set);
	}

	public Iterator<IType> iterator() {
		return types.iterator();
	}

}
