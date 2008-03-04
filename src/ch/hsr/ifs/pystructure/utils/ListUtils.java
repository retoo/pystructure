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

	public static <E> List<E> wrap(E element) {
		List<E> list = new ArrayList<E>();
		list.add(element);
		return list;
	}

}
