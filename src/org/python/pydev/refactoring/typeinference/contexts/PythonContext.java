/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package org.python.pydev.refactoring.typeinference.contexts;

import org.python.pydev.refactoring.typeinference.model.definitions.Module;
import org.python.pydev.refactoring.typeinference.visitors.Workspace;

/**
 * Root context for {@link PythonGoal}s.
 */
public class PythonContext extends AbstractContext {

	private PythonContext parent;
	private Workspace workspace;
	private Module module;
	
	public PythonContext(PythonContext parent, Module module) {
		this(parent.getWorkspace(), module);
		this.parent = parent;
	}
	
	public PythonContext(Workspace workspace, Module module) {
		this.module = module;
		this.workspace = workspace;
	}

	public PythonContext getParent() {
		return parent;
	}
	
	// TODO: Find a better place for this function.
	/**
	 * Get the next call context (may be this).
	 * 
	 * @return the last call context on the "stack"
	 */
	public CallContext getCallContext() {
		PythonContext c;
		for (c = this; c.parent != null; c = c.parent) {
			if (c instanceof CallContext) {
				return (CallContext) c;
			}
		}
		return null;
	}
	
	public Module getModule() {
		return module;
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}

}
