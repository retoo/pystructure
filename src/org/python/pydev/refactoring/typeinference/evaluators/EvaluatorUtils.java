/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.refactoring.typeinference.dltk.types.IEvaluatedType;
import org.python.pydev.refactoring.typeinference.results.types.CombinedType;

public final class EvaluatorUtils {
	private EvaluatorUtils() { }

	public static List<IEvaluatedType> extractTypes(IEvaluatedType type) {
		List<IEvaluatedType> types = new ArrayList<IEvaluatedType>();
		
		if (type instanceof CombinedType) {
			CombinedType combinedType = (CombinedType) type;
			types.addAll(combinedType.getTypes());
		} else {
			types.add(type);
		}
		
		return types;
	}

}
