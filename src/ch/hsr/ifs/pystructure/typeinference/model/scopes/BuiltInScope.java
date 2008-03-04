/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.model.scopes;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;

public class BuiltInScope extends Scope {
	public BuiltInScope() {
		super(null, null);
	}
	
	@Override
	protected List<Definition> getAllDefinitions(NameAdapter name) {
		return new ArrayList<Definition>();
	}
}
