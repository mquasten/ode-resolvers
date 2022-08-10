package de.mq.odesolver.impl;

import java.util.function.Function;

import de.mq.odesolver.OdeResult;

class EulerCalculatorImpl extends AbstractOdeCalculator {

	EulerCalculatorImpl(final Function<OdeResult, Double> odeFunction) {
		super(odeFunction);
	}

	EulerCalculatorImpl(final String odeFunction) {
		super(odeFunction);
	}

	@Override
	double quality() {
		return 1;
	}

	@Override
	double[] calculateFirstOrderOde(final OdeResult last, final double stepSize) {
		return OdeResultImpl.doubleArray(last.yDerivative(0) + stepSize * odeFunction.apply(last));
	}

	@Override
	double[] calculateSecondOrderOde(final OdeResult last, final double stepSize) {
		return OdeResultImpl.doubleArray(last.yDerivative(0) + stepSize * last.yDerivative(1),
				last.yDerivative(1) + stepSize * odeFunction.apply(last));
	}

}
