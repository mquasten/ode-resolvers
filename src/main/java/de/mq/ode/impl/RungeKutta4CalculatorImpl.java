package de.mq.ode.impl;

import static de.mq.ode.impl.OdeResultImpl.doubleArray;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.script.Invocable;

import de.mq.ode.OdeResult;
import de.mq.ode.OdeResultCalculator;

class RungeKutta4CalculatorImpl implements OdeResultCalculator {

	private final Map<Integer, BiFunction<OdeResult, Double, double[]>> calculators = Map.of(1,
			(odeResult, stepSize) -> calculateFirstOrderOde(odeResult, stepSize), 2,
			(odeResult, stepSize) -> calculateSecondOrderOde(odeResult, stepSize));

	private final Function<OdeResult, Double> firstOrderOdeFunction;
	private final OdeStringUtil odeStringUtil = new OdeStringUtil();

	RungeKutta4CalculatorImpl(final Function<OdeResult, Double> firstOrderOdeFunction) {

		this.firstOrderOdeFunction = firstOrderOdeFunction;
	}

	RungeKutta4CalculatorImpl(final String function) {
		final Invocable invocable = odeStringUtil.prepareFunction(function);
		this.firstOrderOdeFunction = firstOrderOdeFunction -> (Double) odeStringUtil.invokeFunction(invocable,
				firstOrderOdeFunction.yDerivatives(), firstOrderOdeFunction.x());
	}

	@Override
	public final double[] calculate(final OdeResult last, final double stepSize) {
		if (!calculators.containsKey(last.order())) {
			throw new IllegalArgumentException(
					String.format("Ode has wrong order: %s. Only first and second order odes supported", last.order()));
		}
		return calculators.get(last.order()).apply(last, stepSize);
	}

	private double[] calculateFirstOrderOde(final OdeResult last, final double stepSize) {
		final double k1 = stepSize
				* this.firstOrderOdeFunction.apply(new OdeResultImpl(doubleArray(last.yDerivative(0)), last.x()));
		final double k2 = stepSize * this.firstOrderOdeFunction
				.apply(new OdeResultImpl(doubleArray(last.yDerivative(0) + k1 / 2), last.x() + stepSize / 2));
		final double k3 = stepSize * this.firstOrderOdeFunction
				.apply(new OdeResultImpl(doubleArray(last.yDerivative(0) + k2 / 2), last.x() + stepSize / 2));
		final double k4 = stepSize * this.firstOrderOdeFunction
				.apply(new OdeResultImpl(doubleArray(last.yDerivative(0) + k3), last.x() + stepSize));

		return doubleArray(last.yDerivative(0) + (k1 + 2 * k2 + 2 * k3 + k4) / 6);
	}

	private double[] calculateSecondOrderOde(final OdeResult last, final double stepSize) {
		final double k1 = stepSize * last.yDerivative(1);
		final double m1 = stepSize * this.firstOrderOdeFunction.apply(new OdeResultImpl(last.yDerivatives(), last.x()));

		final double k2 = stepSize * (last.yDerivative(1) + m1 / 2);
		final double m2 = stepSize * this.firstOrderOdeFunction.apply(new OdeResultImpl(
				doubleArray(last.yDerivative(0) + k1 / 2, last.yDerivative(1) + m1 / 2), last.x() + stepSize / 2));

		final double k3 = stepSize * (last.yDerivative(1) + m2 / 2);
		final double m3 = stepSize * this.firstOrderOdeFunction.apply(new OdeResultImpl(
				doubleArray(last.yDerivative(0) + k2 / 2, last.yDerivative(1) + m2 / 2), last.x() + stepSize / 2));

		final double k4 = stepSize * (last.yDerivative(1) + m3);
		final double m4 = stepSize * this.firstOrderOdeFunction.apply(new OdeResultImpl(
				doubleArray(last.yDerivative(0) + k3, last.yDerivative(1) + m3), last.x() + stepSize));

		return doubleArray(last.yDerivative(0) + (k1 + 2 * k2 + 2 * k3 + k4) / 6,
				last.yDerivative(1) + (m1 + 2 * m2 + 2 * m3 + m4) / 6);
	}

	@Override
	public final double errorEstimaion(final double y, final double y2h) {
		return (y - y2h) / 15d;

	}

}
