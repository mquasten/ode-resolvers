package de.mq.ode.impl;

import de.mq.ode.OdeResult;

class OdeResultImpl implements OdeResult {

	private final double x;
	private final double y[];
	private final double errorEstimaion;

	OdeResultImpl(final double y[], final double x) {
		this(y, x, 0);
	}

	OdeResultImpl(final double y[], final double x, final double errorEstimaion) {
		this.x = x;
		this.y = y;
		this.errorEstimaion = errorEstimaion;
	}

	@Override
	public final double x() {
		return x;
	}

	@Override
	public final double yDerivative(final int n) {
		return y[n];
	}

	@Override
	public final double errorEstimaion() {
		return errorEstimaion;
	}

	public final static double[] doubleArray(final double value) {
		return new double[] { value };

	}

	public final static double[] doubleArray(final double value1, final double value2) {
		return new double[] { value1, value2 };
	}

	@Override
	public double[] yDerivatives() {
		return y;
	}

	@Override
	public int order() {
		return y.length;
	}

}
