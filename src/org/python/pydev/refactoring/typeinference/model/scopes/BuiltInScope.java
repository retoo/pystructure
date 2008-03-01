/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.model.scopes;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.refactoring.typeinference.model.base.NameAdapter;
import org.python.pydev.refactoring.typeinference.model.definitions.Definition;

public class BuiltInScope extends Scope {
	public BuiltInScope() {
		super(null, null);
	}
	
	@Override
	protected List<Definition> getAllDefinitions(NameAdapter name) {
		return new ArrayList<Definition>();
	}
}
