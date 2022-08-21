package de.mq.odesolver.solve.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.mq.odesolver.solve.OdeResultCalculator;
import de.mq.odesolver.solve.support.OdeFunctionUtil.Language;

class EulerCalculatorImplTest {

	private final OdeFunctionUtil odeFunctionUtil = new OdeFunctionUtil(Language.Nashorn);

	private final double maxTol = 1e-15;
	private final OdeResultCalculator odeResultCalculator = new EulerCalculatorImpl(
			last -> last.yDerivative(0) - last.x());

	@Test
	final void calculateforFirstOrderOdeLamda() {
		// Papula Seite 234

		final double[] y1 = odeResultCalculator.calculate(new OdeResultImpl(new double[] { 0 }, 0), 0.05);
		assertEquals(1, y1.length);
		assertEquals(0, y1[0]);

		assertEquals(-0.0025, odeResultCalculator.calculate(new OdeResultImpl(new double[] { 0 }, 0.05), 0.05)[0],
				maxTol);

	}

	@Test
	final void calculateforFirstOrderOdeString() {
		// Papuala Seite 234
		final OdeResultCalculator odeResultCalculator = new EulerCalculatorImpl(odeFunctionUtil, "y[0]-x");
		final double[] y1 = odeResultCalculator.calculate(new OdeResultImpl(new double[] { 0 }, 0), 0.05);
		assertEquals(1, y1.length);
		assertEquals(0, y1[0]);

		assertEquals(-0.0025, odeResultCalculator.calculate(new OdeResultImpl(new double[] { 0 }, 0.05), 0.05)[0],
				maxTol);
	}

	@Test
	final void errorEstimaion() {
		assertEquals(0.1d, odeResultCalculator.errorEstimaion(1.1, 1), maxTol);
	}

	@Test
	final void calculateforSecondOrderOdeLamda() {
		// https://keisan.casio.com/exec/system/1548304004
		// leider ist es wohl zu schwer auch die 1. Ableitung auszugeben!!!

		final OdeResultCalculator odeResultCalculator = new EulerCalculatorImpl(
				last -> -2 * last.yDerivative(1) + 3 * last.yDerivative(0));

		final double[] y1 = odeResultCalculator.calculate(new OdeResultImpl(new double[] { 0, 4 }, 0), 0.1);
		assertEquals(2, y1.length);
		assertEquals(0.4, y1[0]);

		final double[] y2 = odeResultCalculator.calculate(new OdeResultImpl(y1, 0.1), 0.1);
		assertEquals(2, y2.length);
		assertEquals(0.72, y2[0], maxTol);
	}

	@Test
	final void calculateforSecondOrderOdeString() {
		// https://keisan.casio.com/exec/system/1548304004
		// leider ist es wohl zu schwer auch die 1. Ableitung auszugeben!!!
		// daher mit Excel kontrolliert

		final OdeResultCalculator odeResultCalculator = new EulerCalculatorImpl(odeFunctionUtil, "-2*y[1]+3*y[0]");

		final double[] y1 = odeResultCalculator.calculate(new OdeResultImpl(new double[] { 0, 4 }, 0), 0.1);
		assertEquals(2, y1.length);
		assertEquals(0.4, y1[0]);
		assertEquals(3.2, y1[1]);

		final double[] y2 = odeResultCalculator.calculate(new OdeResultImpl(y1, 0.1), 0.1);
		assertEquals(2, y2.length);
		assertEquals(0.72, y2[0], maxTol);
		assertEquals(2.68, y2[1]);
	}

}
