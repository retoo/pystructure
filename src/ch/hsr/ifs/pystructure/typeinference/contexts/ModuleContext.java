/*
 * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
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
	
	public InstanceContext getInstanceContext() {
		ModuleContext c;
		for (c = this; c.parent != null; c = c.parent) {
			if (c instanceof InstanceContext) {
				return (InstanceContext) c;
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
