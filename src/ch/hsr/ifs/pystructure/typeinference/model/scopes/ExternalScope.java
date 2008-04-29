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

package ch.hsr.ifs.pystructure.typeinference.model.scopes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.hsr.ifs.pystructure.typeinference.model.definitions.Definition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImplicitImportDefinition;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.ImportPath;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.utils.ListUtils;

/**
 * Scope for handling definitions which are outside of a module, like "from
 * module import *" or built-in types.
 */
public class ExternalScope extends Scope {

	private final Module module;

	private final LinkedList<ImportPath> importStarPaths;
	private final Map<String, List<Definition>> implicitImportDefinitions;

	public ExternalScope(Module module) {
		super(null);
		this.module = module;
		
		this.importStarPaths = new LinkedList<ImportPath>();
		this.implicitImportDefinitions = new HashMap<String, List<Definition>>();
	}

	public void addImportStarPath(ImportPath importPath) {
		importStarPaths.addFirst(importPath);
	}
	
	@Override
	protected List<Definition> getAllDefinitions(String name) {
		List<Definition> implicitImportDefinition = implicitImportDefinitions.get(name);
		
		if (implicitImportDefinition == null) {
			/*
			 * Copy paths because importStarPaths could change, e.g.:
			 * 
			 * from module1 import *
			 * print A  # for resolving A, only look at module1
			 * from module2 import *
			 */
			List<ImportPath> paths = new LinkedList<ImportPath>(importStarPaths);
			
			Definition def = new ImplicitImportDefinition(module, paths, name);
			implicitImportDefinition = ListUtils.wrap(def);
		}
		
		return implicitImportDefinition;
	}

}
