package de.mq.odesolver.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.mq.odesolver.OdeResultCalculator;

class RungeKutta2CalculatorImplTest {
	
	private final double maxTol = 1e-16;
	// Papuala Seite 235
	private final OdeResultCalculator odeResultCalculator = new RungeKutta2CalculatorImpl(
			last -> last.yDerivative(0) - last.x());

	@Test
	final void calculateforFirstOrderOdeLamda() {
		// Papuala Seite 235
		final double[] y = odeResultCalculator.calculate(new OdeResultImpl(new double[] { 0 }, 0), 0.1);
		assertEquals(1, y.length);
		assertEquals(-0.00500, y[0], maxTol);
	}

	@Test
	final void calculateforFirstOrderOdeString() {
		// Papuala Seite 235
		final OdeResultCalculator odeResultCalculator = new RungeKutta2CalculatorImpl("y[0]-x");
		final double[] y = odeResultCalculator.calculate(new OdeResultImpl(new double[] { 0 }, 0), 0.1);
		assertEquals(1, y.length);
		assertEquals(0. - 0.00500, y[0], maxTol);
	}

	@Test
	final void errorEstimaion() {
		assertEquals(0.1d / 3d, odeResultCalculator.errorEstimaion(1.1, 1), maxTol);
	} 
	
	@Test
	final void calculateforSecondOrderOdeLamda() {
		// https://valdivia.staff.jade-hs.de/solveodesystem.html Heun-Verfahren
		final OdeResultCalculator odeResultCalculator = new RungeKutta2CalculatorImpl(
				last -> -2 * last.yDerivative(1) + 3 * last.yDerivative(0));

		final double[] y = odeResultCalculator.calculate(new OdeResultImpl(new double[] { 0, 4 }, 0), 0.1);
		assertEquals(2, y.length);
		assertEquals(0.36000, y[0], maxTol);
		assertEquals(3.3400, y[1], maxTol);
	}
	
	@Test
	final void calculateforSecondOrderOdeString() {
		final OdeResultCalculator odeResultCalculator = new RungeKutta2CalculatorImpl("-2*y[1]+3*y[0]");

		final double[] y = odeResultCalculator.calculate(new OdeResultImpl(new double[] { 0, 4 }, 0), 0.1);
		assertEquals(2, y.length);
		assertEquals(0.36000, y[0], maxTol);
		assertEquals(3.3400, y[1], maxTol);
	}


}
