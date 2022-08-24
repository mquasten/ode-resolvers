package de.mq.odesolver.function.support;

import java.util.Arrays;

import de.mq.odesolver.Result;

public class FunctionResultImpl implements Result {

	private final double x;
	private final double y[];

	FunctionResultImpl(final double y, final double x) {
		this.y = doubleArray(y);
		this.x = x;
	}

	protected FunctionResultImpl(final double[] y, final double x) {
		this.y = y;
		this.x = x;
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
	public int hashCode() {
		return Arrays.hashCode(y) + Double.hashCode(x);
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Result)) {
			return false;
		}

		final Result other = (Result) obj;
		return Arrays.equals(y, other.yDerivatives()) && x == other.x();
	}

	public final static double[] doubleArray(final double value) {
		return new double[] { value };

	}

	public final static double[] doubleArray(final double value1, final double value2) {
		return new double[] { value1, value2 };
	}

}
