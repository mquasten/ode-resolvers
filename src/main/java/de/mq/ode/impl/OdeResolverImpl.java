package de.mq.ode.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import de.mq.ode.OdeResolver;
import de.mq.ode.OdeResult;
import de.mq.ode.OdeResultCalculator;

class OdeResolverImpl implements OdeResolver {

	private final OdeResultCalculator odeResultCalculator;

	OdeResolverImpl(final OdeResultCalculator odeResultCalculator) {
		this.odeResultCalculator = odeResultCalculator;
	}

	@Override
	public final List<OdeResult> solve(final double[] y0, final double start, final double stop, final int steps) {
		final List<OdeResult> results = new ArrayList<>();
		final double stepSize = (stop - start) / steps;
		results.add(new OdeResultImpl(y0, start, 0));
		IntStream.rangeClosed(1, steps).boxed().forEach(n -> {
			final OdeResult last = results.get(n - 1);
			final double[] y = odeResultCalculator.calculate(last, stepSize);
			final double[] y2h = odeResultCalculator.calculate(last, 2 * stepSize);
			final double errorEstimation = odeResultCalculator.errorEstimaion(y[0], y2h[0]);
			results.add(new OdeResultImpl(y, last.x() + stepSize, errorEstimation));
		});
		return results;
	}

}
