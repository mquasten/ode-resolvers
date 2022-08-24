package de.mq.odesolver.function.support;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import de.mq.odesolver.Result;

class FunctionResultImplTest {

	private final double y = 2d;
	private final double x = 3d;

	final Result result = new FunctionResultImpl(y, x);

	@Test
	void x() {
		assertEquals(x, result.x());
	}

	@Test
	void yDerivative() {
		assertEquals(y, result.yDerivative(0));
	}

	@Test
	void yDerivativeWrongIndex() {
		assertThrows(IllegalArgumentException.class, () -> result.yDerivative(1));
	}

	@Test
	void yDerivatives() {
		assertArrayEquals(FunctionResultImpl.doubleArray(y), result.yDerivatives());
	}

	@Test
	void hash() {

		assertEquals(Arrays.hashCode(FunctionResultImpl.doubleArray(y)) + Double.hashCode(x),
				new FunctionResultImpl(y, x).hashCode());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void equals() {
		Result first = new FunctionResultImpl(y, x);
		assertTrue(first.equals(first));
		assertTrue(first.equals(new FunctionResultImpl(y, x)));
		assertFalse(first.equals(new FunctionResultImpl(y, x + x)));
		assertTrue(first.equals(new FunctionResultImpl(new double[] { y }, x)));
		assertFalse(first.equals(""));
	}

	@Test
	void doubleArray() {
		assertArrayEquals(new double[] { y }, FunctionResultImpl.doubleArray(y));
	}

	@Test
	void doubleArray2Values() {
		assertArrayEquals(new double[] { y, x }, FunctionResultImpl.doubleArray(y, x));
	}

}
