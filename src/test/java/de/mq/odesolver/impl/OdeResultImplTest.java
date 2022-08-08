package de.mq.odesolver.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import de.mq.odesolver.OdeResult;

class OdeResultImplTest {

	private final double[] y = new double[] { 1d, 2d };
	private final double x = 3d;

	private final double errorEstimation = 3d;

	private final OdeResult odeResult = new OdeResultImpl(y, x, errorEstimation);

	@Test
	void x() {
		assertEquals(x, odeResult.x());
	}

	@Test
	void yDerivative() {
		IntStream.range(0, y.length).forEach(n -> assertEquals(y[n], odeResult.yDerivative(n)));
	}

	@Test
	void yDerivativeWrongDerivativeLessThanZero() {
		assertThrows(IllegalArgumentException.class, () -> odeResult.yDerivative(-1));
	}

	@Test
	void yDerivativeWrongDerivativeLess() {
		assertThrows(IllegalArgumentException.class, () -> odeResult.yDerivative(2));
	}

	@Test
	void yDerivatives() {
		assertEquals(y, odeResult.yDerivatives());
	}

	@Test
	void order() {
		assertEquals(2, odeResult.order());
	}

	@Test
	void errorEstimaion() {
		assertEquals(errorEstimation, odeResult.errorEstimaion());
		assertEquals(0, new OdeResultImpl(y, x).errorEstimaion());
	}

	@Test
	void doubleArray() {
		assertArrayEquals(new double[] { y[0] }, OdeResultImpl.doubleArray(y[0]));
	}

	@Test
	void doubleArray2Values() {
		assertArrayEquals(y, OdeResultImpl.doubleArray(y[0], y[1]));
	}

	@Test
	void hash() {
		assertEquals(Arrays.hashCode(y) + Double.hashCode(x), new OdeResultImpl(y, x).hashCode());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void equals() {
		OdeResult first = new OdeResultImpl(y, x);
		assertTrue(first.equals(first));
		assertTrue(first.equals(new OdeResultImpl(y, x)));
		assertFalse(first.equals(new OdeResultImpl(y, x + x)));
		assertFalse(first.equals(new OdeResultImpl(new double[] { y[0] }, x)));
		assertFalse(first.equals(new OdeResultImpl(new double[] { y[0] }, x + x)));
		assertFalse(first.equals(""));
	}

}
