/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.typeinference.contexts;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

/**
 * Root context for {@link AbstractGoal}s.
 */
public class ModuleContext {

	private ModuleContext parent;
	private Workspace workspace;
	private Module module;
	
	public ModuleContext(ModuleContext parent, Module module) {
		this(parent.getWorkspace(), module);
		this.parent = parent;
	}
	
	public ModuleContext(Workspace workspace, Module module) {
		this.module = module;
		this.workspace = workspace;
	}

	public ModuleContext getParent() {
		return parent;
	}
	
	// TODO: Find a better place for this function.
	/**
	 * Get the next call context (may be this).
	 * 
	 * @return the last call context on the "stack"
	 */
	public CallContext getCallContext() {
		ModuleContext c;
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
	
	@Override
	public String toString() {
		return "Module " + module.getName();
	}

}
