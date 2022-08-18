package de.mq.odesolver.solve.support;

import static de.mq.odesolver.solve.support.OdeResultImpl.doubleArray;

import java.util.function.Function;

import de.mq.odesolver.solve.OdeResult;

class RungeKutta4CalculatorImpl extends AbstractOdeCalculator {

	
	RungeKutta4CalculatorImpl(final Function<OdeResult, Double> firstOrderOdeFunction) {
		super(firstOrderOdeFunction);
	}

	RungeKutta4CalculatorImpl(final String function) {
		super(function);
	}
		

	

	double[] calculateFirstOrderOde(final OdeResult last, final double stepSize) {
		final double k1 = stepSize
				* this.odeFunction.apply(new OdeResultImpl(doubleArray(last.yDerivative(0)), last.x()));
		final double k2 = stepSize * this.odeFunction
				.apply(new OdeResultImpl(doubleArray(last.yDerivative(0) + k1 / 2), last.x() + stepSize / 2));
		final double k3 = stepSize * this.odeFunction
				.apply(new OdeResultImpl(doubleArray(last.yDerivative(0) + k2 / 2), last.x() + stepSize / 2));
		final double k4 = stepSize * this.odeFunction
				.apply(new OdeResultImpl(doubleArray(last.yDerivative(0) + k3), last.x() + stepSize));

		return doubleArray(last.yDerivative(0) + (k1 + 2 * k2 + 2 * k3 + k4) / 6);
	}

	double[] calculateSecondOrderOde(final OdeResult last, final double stepSize) {
		final double k1 = stepSize * last.yDerivative(1);
		final double m1 = stepSize * this.odeFunction.apply(new OdeResultImpl(last.yDerivatives(), last.x()));

		final double k2 = stepSize * (last.yDerivative(1) + m1 / 2);
		final double m2 = stepSize * this.odeFunction.apply(new OdeResultImpl(
				doubleArray(last.yDerivative(0) + k1 / 2, last.yDerivative(1) + m1 / 2), last.x() + stepSize / 2));

		final double k3 = stepSize * (last.yDerivative(1) + m2 / 2);
		final double m3 = stepSize * this.odeFunction.apply(new OdeResultImpl(
				doubleArray(last.yDerivative(0) + k2 / 2, last.yDerivative(1) + m2 / 2), last.x() + stepSize / 2));

		final double k4 = stepSize * (last.yDerivative(1) + m3);
		final double m4 = stepSize * this.odeFunction.apply(new OdeResultImpl(
				doubleArray(last.yDerivative(0) + k3, last.yDerivative(1) + m3), last.x() + stepSize));

		return doubleArray(last.yDerivative(0) + (k1 + 2 * k2 + 2 * k3 + k4) / 6,
				last.yDerivative(1) + (m1 + 2 * m2 + 2 * m3 + m4) / 6);
	}

	@Override
	double quality() {
		return 01d/15d;
	}

	

}
