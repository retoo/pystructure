/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.python.pydev.refactoring.typeinference.dltk.goals;

import org.python.pydev.refactoring.typeinference.dltk.contexts.IContext;

public abstract class AbstractGoal implements IGoal {

	protected final IContext context;

	public AbstractGoal(IContext context) {
		this.context = context;
	}

	public IContext getContext() {
		return context;
	}
}
