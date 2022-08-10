package de.mq.odesolver.impl;

import static de.mq.odesolver.impl.OdeResultImpl.doubleArray;

import java.util.function.Function;

import de.mq.odesolver.OdeResult;

class RungeKutta2CalculatorImpl  extends AbstractOdeCalculator{

	RungeKutta2CalculatorImpl(final Function<OdeResult, Double> odeFunction) {
		super(odeFunction);
	}
	
	RungeKutta2CalculatorImpl(final String odeFunctionAsString) {
		super(odeFunctionAsString);
	}

	@Override
	double quality() {
		return 1d/3;
	}

	@Override
	double[] calculateFirstOrderOde(OdeResult last, double stepSize) {
		final double  k1=stepSize*this.odeFunction.apply(new OdeResultImpl(last.yDerivatives(), last.x()));
		final double  k2=stepSize*this.odeFunction.apply(new OdeResultImpl(doubleArray(last.yDerivative(0)+k1), last.x()+stepSize));
		return doubleArray(last.yDerivative(0) + (k1 + k2) / 2);
	}

	@Override
	double[] calculateSecondOrderOde(OdeResult last, double stepSize) {
		final double k1=stepSize*last.yDerivative(1);
		final double  m1=stepSize*this.odeFunction.apply(new OdeResultImpl(last.yDerivatives(), last.x()));
		
		final double k2=stepSize*(last. yDerivative(1)+m1);
		final double  m2=stepSize*this.odeFunction.apply(new OdeResultImpl(doubleArray(last.yDerivative(0)+k1, last.yDerivative(1)+m1), last.x()+stepSize));
		return  doubleArray(last.yDerivative(0) + (k1 + k2) / 2 , last.yDerivative(1) + (m1 + m2) / 2 );
	}

}
