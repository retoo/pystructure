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

package ch.hsr.ifs.pystructure.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class IteratorChain<E> implements Iterator<E> {

	private List<Iterator<E>> iterators;
	private Iterator<E> currentIterator;
	private int nextIndex;
	
	public IteratorChain() {
		iterators = new LinkedList<Iterator<E>>();
		currentIterator = null;
		nextIndex = 1;
	}
	
	public void add(Iterable<E> iterable) {
		iterators.add(iterable.iterator());
		if (currentIterator == null) {
			currentIterator = iterable.iterator();
		}
	}
	
	public boolean hasNext() {
		return currentIterator != null && (currentIterator.hasNext() || nextIndex < iterators.size());
	}

	public E next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		E result = currentIterator.next();
		if (!currentIterator.hasNext() && nextIndex < iterators.size()) {
			currentIterator = iterators.get(nextIndex);
			nextIndex++;
		}
		return result;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
