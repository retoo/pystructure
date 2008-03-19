/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.types;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.AbstractGoal;

public abstract class AbstractTypeGoal extends AbstractGoal {
	public final CombinedType resultType;
	
	public AbstractTypeGoal(ModuleContext context) {
		super(context);
		resultType = new CombinedType();
	}

}
