package de.mq.odesolver.impl;

import java.util.List;
import java.util.Map;

import de.mq.odesolver.OdeResolver;
import de.mq.odesolver.OdeResult;
import de.mq.odesolver.OdeResultCalculator;

class OdeResolverServiceImpl implements OdeResolverService {

	static final double DELTA = 1e-3;
	private final Map<Algorithm, Class<? extends OdeResultCalculator>> solvers = Map.of(Algorithm.EulerPolygonal,
			EulerCalculatorImpl.class, Algorithm.RungeKutta2ndOrder, RungeKutta2CalculatorImpl.class,
			Algorithm.RungeKutta4ndOrder, RungeKutta4CalculatorImpl.class);

	@Override
	public final OdeResolver odeResolver(final Algorithm algorithm, final String function) {

		try {
			final OdeResultCalculator odeResultCalculator = solvers.get(algorithm).getDeclaredConstructor(String.class)
					.newInstance(function);
			return new OdeSolverImpl(odeResultCalculator);
		} catch (Exception exception) {
			throw new IllegalStateException(exception);
		}

	}

	@Override
	public final List<OdeResult> solve(final OdeResolver odeResolver, final OdeResult start, final double end,
			final int steps) {
		return odeResolver.solve(start.yDerivatives(), start.x(), end, steps);
	}

	@Override
	public void validate(final OdeResolver odeResolver, final OdeResult start) {
		odeResolver.solve(start.yDerivatives(), start.x(), start.x() + DELTA, 2);
	}

}
