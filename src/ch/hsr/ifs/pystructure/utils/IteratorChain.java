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
		nextIndex = 1;
	}
	
	public void addIterator(Iterator<E> iterator) {
		iterators.add(iterator);
		if (currentIterator == null) {
			currentIterator = iterator;
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
