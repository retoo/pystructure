/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
/**
 *
 */
package ch.hsr.ifs.pystructure.typeinference.dltk.goals;

import org.python.pydev.parser.jython.SimpleNode;

@Deprecated
public class PossiblePosition {
//	private final IResource resource;
	private final int offset;
	private final int length;
	private final SimpleNode node;

	public PossiblePosition(IResource resource, int offset, int length) {
	super();
//	this.resource = resource;
	this.offset = offset;
	this.length = length;
	this.node = null;
	}

//	public PossiblePosition(IResource resource, int offset, int length, SimpleNode node) {
	public PossiblePosition(IResource resource, int offset, int length, SimpleNode node) {		
	super();
//	this.resource = resource;
	this.offset = offset;
	this.length = length;
	this.node = node;
	}

//	public IResource getResource() {
//	return resource;
//	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}

	/**
	 * Node could be null
	 */
	public SimpleNode getNode() {
		return node;
	}
}
