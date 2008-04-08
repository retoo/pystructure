/*
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 *
 */

package ch.hsr.ifs.pystructure.tests.utils;

import java.util.Arrays;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import ch.hsr.ifs.pystructure.utils.IteratorChain;

public class IteratorChainTest extends TestCase {

	public void testNormal() {
		IteratorChain<String> chain = new IteratorChain<String>();
		chain.add(Arrays.asList("1", "2", "3"));
		chain.add(Arrays.asList("4", "5"));
		
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
		
		try {
			chain.next();
			fail("Expected NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
	}
	
	public void testEmpty() {
		IteratorChain<String> chain = new IteratorChain<String>();
		
		assertFalse(chain.hasNext());
		
		try {
			chain.next();
			fail("Expected NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
	}
	
	public void testRemove() {
		IteratorChain<String> chain = new IteratorChain<String>();
		chain.add(Arrays.asList("1", "2", "3"));
		
		try {
			chain.remove();
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
	}
	
}
