/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.python.pydev.refactoring.typeinference.dltk.types;

public interface IEvaluatedType {

	String getTypeName();

	/**
	 * Returns <code>true</code> if it is subtype of a given type
	 */
	boolean subtypeOf(IEvaluatedType type);
}
