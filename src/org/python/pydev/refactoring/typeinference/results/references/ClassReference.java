/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.results.references;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.refactoring.typeinference.model.definitions.Class;

public class ClassReference extends Reference {
	
	public ClassReference(Class definition, SimpleNode node) {
		super(definition, node);
	}
	
}
