/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.tests.utils;

import java.util.Arrays;

import ch.hsr.ifs.pystructure.playground.IteratorChain;
import junit.framework.TestCase;

public class IteratorChainTest extends TestCase {

	public void testIteratorChain() {
		IteratorChain<String> chain = new IteratorChain<String>();
		chain.addIterator(Arrays.asList("1", "2", "3").iterator());
		chain.addIterator(Arrays.asList("4", "5").iterator());
		
		assertTrue(chain.hasNext());
		assertEquals("1", chain.next());
		assertTrue(chain.hasNext());
		assertEquals("2", chain.next());
		assertTrue(chain.hasNext());
		assertEquals("3", chain.next());
		assertTrue(chain.hasNext());
		assertEquals("4", chain.next());
		assertTrue(chain.hasNext());
		assertEquals("5", chain.next());
		
		assertFalse(chain.hasNext());
	}
	
}
