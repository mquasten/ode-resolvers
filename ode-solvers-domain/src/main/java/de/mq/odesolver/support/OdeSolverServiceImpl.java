package de.mq.odesolver.support;

import java.util.Map;

import javax.script.Invocable;

import de.mq.odesolver.OdeSolver;
import de.mq.odesolver.OdeResult;
import de.mq.odesolver.OdeResultCalculator;
import de.mq.odesolver.OdeSolverService;

class OdeSolverServiceImpl implements OdeSolverService {

	static final double DELTA = 1e-3;
	private final Map<Algorithm, Class<? extends OdeResultCalculator>> solvers = Map.of(Algorithm.EulerPolygonal,
			EulerCalculatorImpl.class, Algorithm.RungeKutta2ndOrder, RungeKutta2CalculatorImpl.class,
			Algorithm.RungeKutta4ndOrder, RungeKutta4CalculatorImpl.class);

	private final OdeFunctionUtil odeFunctionUtil;
	OdeSolverServiceImpl(final OdeFunctionUtil odeFunctionUtil) {
		this.odeFunctionUtil=odeFunctionUtil;
	}
	
	@Override
	public final OdeSolver odeResolver(final Algorithm algorithm, final String function) {

		try {
			final OdeResultCalculator odeResultCalculator = solvers.get(algorithm).getDeclaredConstructor(String.class)
					.newInstance(function);
			return new OdeSolverImpl(odeResultCalculator);
		} catch (Exception exception) {
			throw new IllegalStateException(exception.getCause());
		}

	}

	

	@Override
	public final double validateRightSide(final String function, final double y0[], final double x0) {
		final Invocable invocable= odeFunctionUtil.prepareFunction(function);
		return odeFunctionUtil.invokeFunction(invocable, y0, x0);
	}

}
