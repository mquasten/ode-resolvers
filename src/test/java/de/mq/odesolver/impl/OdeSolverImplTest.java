package de.mq.odesolver.impl;

import static de.mq.odesolver.impl.OdeResultImpl.doubleArray;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mq.odesolver.OdeResolver;
import de.mq.odesolver.OdeResult;
import de.mq.odesolver.OdeResultCalculator;

public class OdeSolverImplTest {

	private final OdeResultCalculator odeResultCalculator = Mockito.mock(OdeResultCalculator.class);

	private final OdeResolver odeSolver = new OdeSolverImpl(odeResultCalculator);

	@Test
	void solve() {
		// y''=1, y(0)=-1, y'(0)=-1, spez. Loesung: y=1/2x^2+x-1 (y'=x+1)
		when(odeResultCalculator.calculate(any(), anyDouble())).thenReturn(y(0.25), y(0.25), y(0.5), y(0.5), y(0.75),
				y(0.75), y(1), y(1));
		final double[] y0 = new double[] { -1, 1 };
		final List<OdeResult> results = odeSolver.solve(y0, 0, 1, 4);

		assertEquals(5, results.size());
		assertArrayEquals(y0, results.get(0).yDerivatives());

		IntStream.range(1, results.size()).forEach(n -> assertArrayEquals(y(results.get(n).x()), results.get(n).yDerivatives()));
		IntStream.range(0, results.size()).forEach(n -> assertEquals(n / 4d, results.get(n).x()));
		IntStream.range(0, results.size()).forEach(n -> assertEquals(0, results.get(n).errorEstimaion()));

		verify(odeResultCalculator, times(8)).calculate(any(), anyDouble());

	}
	

	private double[] y(final double x) {
		// spez. Loesung AWP siehe solve
		return doubleArray(x * x / 2 + x - 1, x + 1);

	}
}
