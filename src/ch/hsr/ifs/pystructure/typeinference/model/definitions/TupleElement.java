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

package ch.hsr.ifs.pystructure.typeinference.model.definitions;

import java.util.LinkedList;
import java.util.List;

import org.python.pydev.parser.jython.ast.exprType;

public class TupleElement extends Value {
	
	private final LinkedList<Integer> indexes;

	public TupleElement(exprType expression, List<Integer> indexes) {
		super(expression);
		this.indexes = new LinkedList<Integer>(indexes);
	}
	
	public int getFirstIndex() {
		return indexes.getFirst();
	}
	
	public List<Integer> getRestOfIndexes() {
		return indexes.subList(1, indexes.size());
	}

	public int getIndexesCount() {
		return indexes.size();
	}
}
