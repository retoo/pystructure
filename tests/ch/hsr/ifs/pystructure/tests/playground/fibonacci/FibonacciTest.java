package ch.hsr.ifs.pystructure.tests.playground.fibonacci;

import ch.hsr.ifs.pystructure.playground.fibonacci.Fibonacci;
import junit.framework.TestCase;

public class FibonacciTest extends TestCase {
		
	public void testFibbonacci() {
		Fibonacci fib = new Fibonacci();
		
		assertEquals(0, fib.calculate(0));
		assertEquals(1, fib.calculate(1));
		assertEquals(1, fib.calculate(2));
		assertEquals(2, fib.calculate(3));
		assertEquals(3, fib.calculate(4));
		assertEquals(5, fib.calculate(5));
		assertEquals(8, fib.calculate(6));
		assertEquals(13, fib.calculate(7));
		assertEquals(21, fib.calculate(8));
		assertEquals(34, fib.calculate(9));
		assertEquals(55, fib.calculate(10));
		assertEquals(89, fib.calculate(11));
		assertEquals(144, fib.calculate(12));
		assertEquals(233, fib.calculate(13));
		assertEquals(377, fib.calculate(14));
		assertEquals(610, fib.calculate(15));
		assertEquals(987, fib.calculate(16));
		assertEquals(1597, fib.calculate(17));
		assertEquals(2584, fib.calculate(18));
		assertEquals(4181, fib.calculate(19));
		
		fib.shutdown();	
	}
	
}
