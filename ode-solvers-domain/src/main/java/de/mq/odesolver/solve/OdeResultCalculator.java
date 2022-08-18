package de.mq.odesolver.solve;

public interface OdeResultCalculator {

	double[] calculate(final OdeResult last, final double stepSize);

	double errorEstimaion(final double y, final double y2h);
	
}
