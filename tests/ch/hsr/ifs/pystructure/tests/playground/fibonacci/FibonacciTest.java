package ch.hsr.ifs.pystructure.tests.playground.fibonacci;

import ch.hsr.ifs.pystructure.playground.fibonacci.Fibonacci;
import junit.framework.TestCase;

public class FibonacciTest extends TestCase {
		
	public void testFibbonacci() {
		Fibonacci fib = new Fibonacci();
		
		assertEquals(0, fib.calc(0));
		assertEquals(1, fib.calc(1));
		assertEquals(1, fib.calc(2));
		assertEquals(2, fib.calc(3));
		assertEquals(3, fib.calc(4));
		assertEquals(5, fib.calc(5));
		assertEquals(8, fib.calc(6));
		assertEquals(13, fib.calc(7));
		assertEquals(21, fib.calc(8));
		assertEquals(34, fib.calc(9));
		assertEquals(55, fib.calc(10));
		assertEquals(89, fib.calc(11));
		assertEquals(144, fib.calc(12));
		assertEquals(233, fib.calc(13));
		assertEquals(377, fib.calc(14));
		assertEquals(610, fib.calc(15));
		assertEquals(987, fib.calc(16));
		assertEquals(1597, fib.calc(17));
		assertEquals(2584, fib.calc(18));
		assertEquals(4181, fib.calc(19));
		
		fib.shutdown();	
	}
	
}
