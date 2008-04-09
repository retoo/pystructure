/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.basetype;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.hsr.ifs.pystructure.utils.StringUtils;

public class CombinedType implements IType, Iterable<IType> {

	private Set<IType> types;
	
	public CombinedType() {
		types = new HashSet<IType>();
	}
	
	public void appendType(IType type) {
		if (type instanceof CombinedType) {
			CombinedType combinedType = (CombinedType) type;
			types.addAll(combinedType.types);
		} else {
			types.add(type);
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
		return getTypeName();
	}

	public Iterator<IType> iterator() {
		return types.iterator();
	}
}
