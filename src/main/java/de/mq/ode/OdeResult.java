package de.mq.ode;

public interface OdeResult {

	double x();

	double yDerivative(final int n);

	double[] yDerivatives();

	double errorEstimaion();

	int order();

}