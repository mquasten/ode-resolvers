package de.mq.odesolver;

public interface OdeResult {

	double x();

	double yDerivative(final int n);

	double[] yDerivatives();

	double errorEstimaion();

	int order();

}