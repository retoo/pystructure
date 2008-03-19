/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.goals.references;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.ifs.pystructure.typeinference.contexts.ModuleContext;
import ch.hsr.ifs.pystructure.typeinference.model.base.NameAdapter;
import ch.hsr.ifs.pystructure.typeinference.results.references.AttributeReference;

public class PossibleAttributeReferencesGoal extends PossibleReferencesGoal {

	public final List<AttributeReference> possibleReferences;

	public PossibleAttributeReferencesGoal(ModuleContext context, NameAdapter name) {
		super(context, name);
		/* FIXME: the parent class has a field 'references', should we reuse that? */
		this.possibleReferences = new ArrayList<AttributeReference>();
	}

}
