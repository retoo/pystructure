/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
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

public class CombinedType implements IEvaluatedType, Iterable<IEvaluatedType> {

	private Set<IEvaluatedType> types;
	
	public CombinedType() {
		types = new HashSet<IEvaluatedType>();
	}
	
	public void appendType(IEvaluatedType type) {
		if (type instanceof CombinedType) {
			CombinedType combinedType = (CombinedType) type;
			types.addAll(combinedType.types);
		} else {
			types.add(type);
		}
	}
	
	public Set<IEvaluatedType> getTypes() {
		return types;
	}
	
	public String getTypeName() {
		SortedSet<String> set = new TreeSet<String>();
		for (IEvaluatedType type : types) {
			set.add(type.getTypeName());
		}
		return StringUtils.join('|', set);
	}

	public boolean subtypeOf(IEvaluatedType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return getTypeName();
	}

	public Iterator<IEvaluatedType> iterator() {
		return types.iterator();
	}
}
