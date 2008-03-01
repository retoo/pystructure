/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.python.pydev.refactoring.typeinference.dltk.types;

/**
 * Represents type as some user class
 * Each such class should be presented inside a DLTK MixinModel.
 */
public abstract class ClassType implements IEvaluatedType {

	public String getTypeName() {
		return null;
	}

	public abstract String getModelKey();

	public String toString() {
		return getModelKey();
	}

}
