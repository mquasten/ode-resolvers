package de.mq.odesolver.solve.support;

import static de.mq.odesolver.support.OdeFunctionUtilFactory.newOdeFunctionUtil;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.mq.odesolver.solve.OdeResultCalculator;
import de.mq.odesolver.support.OdeFunctionUtil.Language;
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

	@ParameterizedTest
	@EnumSource
	final void calculateforFirstOrderOdeString(final Language language) {
		// Papuala Seite 235
		final OdeResultCalculator odeResultCalculator = new RungeKutta2CalculatorImpl(newOdeFunctionUtil(language),
				"y[0]-x");
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

	@ParameterizedTest
	@EnumSource
	final void calculateforSecondOrderOdeString(final Language language) {
		final OdeResultCalculator odeResultCalculator = new RungeKutta2CalculatorImpl(newOdeFunctionUtil(language),
				"-2*y[1]+3*y[0]");

		final double[] y = odeResultCalculator.calculate(new OdeResultImpl(new double[] { 0, 4 }, 0), 0.1);
		assertEquals(2, y.length);
		assertEquals(0.36000, y[0], maxTol);
		assertEquals(3.3400, y[1], maxTol);
	}

}
