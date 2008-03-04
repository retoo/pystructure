/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.evaluators;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.dltk.types.IEvaluatedType;
import ch.hsr.ifs.pystructure.typeinference.results.types.CombinedType;

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
