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
