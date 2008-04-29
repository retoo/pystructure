/*
 * Copyright (C) 2008  Reto Schuettel, Robin Stocker
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

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.List;

/**
 * Definition for implicitly imported things, like names from "from module
 * import *" and built-in types and functions.
 */
public class ImplicitImportDefinition extends Definition {

	private final List<ImportPath> importStarPaths;

	public ImplicitImportDefinition(Module module, List<ImportPath> importStarPaths, String name) {
		super(module, name, null);
		this.importStarPaths = importStarPaths;
	}

	public List<ImportPath> getImportStarPaths() {
		return importStarPaths;
	}

}
