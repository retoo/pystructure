/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.utils;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {
	
	private ListUtils() {
	}

	/**
	 * Wraps a single goal in a list and returns it, for convenience.
	 * @param goal
	 * @return a list with the goal as the only element
	 */
	public static <E> List<E> wrap(E element) {
		List<E> list = new ArrayList<E>();
		list.add(element);
		return list;
	}

}
