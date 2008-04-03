package ch.hsr.ifs.pystructure.playground;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class IteratorChain<T> implements Iterator<T> {

	private List<Iterator<T>> iterators;
	private Iterator<T> currentIterator;
	private int index;
	
	public IteratorChain() {
		iterators = new LinkedList<Iterator<T>>();
		index = 0;
	}
	
	public void addIterator(Iterator<T> iterator) {
		iterators.add(iterator);
	}
	
	public boolean hasNext() {
		return !iterators.isEmpty() && iterators.get(index).hasNext();
	}

	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		T result = iterators.get(index).next();
//		if (!currentIterator.hasNext() && iteratorsIterator.hasNext()) {
//			currentIterator = iteratorsIterator.next();
//		}
		return result;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
