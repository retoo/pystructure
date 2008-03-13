/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.references;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;

public class PossibleAttributeReferencesGoal extends PossibleReferencesGoal {

	public PossibleAttributeReferencesGoal(ModuleContext context, NameAdapter name) {
		super(context, name);
	}

}
