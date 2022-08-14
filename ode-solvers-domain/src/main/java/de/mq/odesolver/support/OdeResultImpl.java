package de.mq.odesolver.support;

import java.util.Arrays;

import de.mq.odesolver.OdeResult;

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
		derivativeGuard(n);
		return y[n];
	}

	private void derivativeGuard(final int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Derivative must be >= 0.");
		}
		if (n >= y.length) {
			throw new IllegalArgumentException(String.format("Derivative must be < %d", y.length));
		}
	}

	@Override
	public double[] yDerivatives() {
		return y;
	}

	@Override
	public int order() {
		return y.length;
	}

	@Override
	public final double errorEstimaion() {
		return errorEstimaion;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(y) + Double.hashCode(x);
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof OdeResult)) {
			return false;
		}

		final OdeResult other = (OdeResult) obj;
		return Arrays.equals(y, other.yDerivatives()) && x == other.x();
	}

	final static double[] doubleArray(final double value) {
		return new double[] { value };

	}

	final static double[] doubleArray(final double value1, final double value2) {
		return new double[] { value1, value2 };
	}

}
