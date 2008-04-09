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
import ch.hsr.ifs.pystructure.typeinference.results.references.FunctionReference;

/**
 * Context of a call. This makes it possible to find the type of an argument
 * in a context-sensitive manner:
 * 
 * <code>
 * def func(x):
 *     return x
 * 
 * func(1) ## type int
 * func(1.1) ## type float
 * </code>
 * 
 * Without the call context, both results would be float|int.
 */
public class CallContext extends ModuleContext {

	private final FunctionReference functionReference;
	
	public CallContext(ModuleContext parent, Module module, FunctionReference functionReference) {
		super(parent, module);
		this.functionReference = functionReference;
	}
	
	public FunctionReference getFunctionReference() {
		return functionReference;
	}

}
