/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package ch.hsr.ifs.pystructure.typeinference.dltk.types;

import java.util.Collection;
import java.util.HashSet;

/**
 * Combination of some set of simple types.
 */
public class CombinedType implements IEvaluatedType {

	private Collection types = new HashSet();

	public String getTypeName() {
		return null;
	}

	public void appendType(IEvaluatedType type) {
		if (type instanceof CombinedType) {
			CombinedType combinedType = (CombinedType) type;
			types.addAll(combinedType.types);
		} else {
			types.add(type);
		}
	}

	public IEvaluatedType[] getTypes() {
		return (IEvaluatedType[]) types
				.toArray(new IEvaluatedType[types.size()]);
	}

	/**
	 * Combines types and returns one concrete type. If set contains only one
	 * type, it will be returned. If set contains no types, then
	 * <code>null</code> will be return. In other cases
	 * <code>MostSpecificType</code> are returned. If you want to use more
	 * complicated combination algorithm, for ex. intersection of types or their
	 * union, you may derive this class and override this method.
	 */
	public IEvaluatedType getCombinedType() {
		if (types.size() == 0) {
			return null;
		}
		if (types.size() == 1) {
			return (IEvaluatedType) types.iterator().next();
		}
		return MostSpecificType.getInstance();
	}

	public boolean subtypeOf(IEvaluatedType type) {
		IEvaluatedType combinedType = getCombinedType();
		if (combinedType == null) {
			return false;
		}
		return combinedType.subtypeOf(type);
	}

}
