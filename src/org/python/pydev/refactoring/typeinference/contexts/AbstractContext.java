/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.contexts;

import org.python.pydev.refactoring.typeinference.dltk.contexts.IContext;

public class AbstractContext implements IContext {

	public String getLangNature() {
		return null;
	}

}
