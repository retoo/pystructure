/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.goals.references;

import org.python.pydev.refactoring.typeinference.contexts.PythonContext;
import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;

public class PossibleAttributeReferencesGoal extends PossibleReferencesGoal {

	public PossibleAttributeReferencesGoal(PythonContext context, NameAdapter name) {
		super(context, name);
	}

}
