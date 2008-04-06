package ch.hsr.ifs.pystructure.utils;

import java.util.Iterator;

public class IterableIterator<E> implements Iterable<E> {

	private final Iterator<E> iterator;

	public IterableIterator(Iterator<E> iterator) {
		this.iterator = iterator;
	}

	public Iterator<E> iterator() {
		return iterator;
	}

}
