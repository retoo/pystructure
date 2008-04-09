/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.references;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.goals.base.AbstractGoal;
import ch.hsr.ifs.pystructure.typeinference.results.references.FunctionReference;

public abstract class CallableGoal extends AbstractGoal {
	public final List<FunctionReference> references;

	public CallableGoal(ModuleContext context) {
		super(context);
		references = new ArrayList<FunctionReference>();
	}

}
