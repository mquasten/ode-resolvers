package de.mq.odesolver.solve.support;

import java.util.Map;

import javax.script.Invocable;

import de.mq.odesolver.solve.OdeResultCalculator;
import de.mq.odesolver.solve.OdeSolver;
import de.mq.odesolver.solve.OdeSolverService;
import de.mq.odesolver.support.OdeFunctionUtil;

class OdeSolverServiceImpl implements OdeSolverService {

	private final Map<Algorithm, Class<? extends OdeResultCalculator>> solvers = Map.of(Algorithm.EulerPolygonal, EulerCalculatorImpl.class, Algorithm.RungeKutta2ndOrder,
			RungeKutta2CalculatorImpl.class, Algorithm.RungeKutta4thOrder, RungeKutta4CalculatorImpl.class);

	private final OdeFunctionUtil odeFunctionUtil;

	OdeSolverServiceImpl(final OdeFunctionUtil odeFunctionUtil) {
		this.odeFunctionUtil = odeFunctionUtil;
	} 
	
	
	

	@Override
	public final OdeSolver odeSolver(final Algorithm algorithm, final String function) {
		try {
			final OdeResultCalculator odeResultCalculator = solvers.get(algorithm).getDeclaredConstructor(OdeFunctionUtil.class, String.class).newInstance(odeFunctionUtil, function);
			return new OdeSolverImpl(odeResultCalculator);
		} catch (Exception exception) {
			throw new IllegalStateException(exception.getCause());
		}

	}

	@Override
	public final double validateRightSide(final String function, final double y0[], final double x0) {
		final Invocable invocable = odeFunctionUtil.prepareFunction(function);
		return odeFunctionUtil.invokeFunction(invocable, y0, x0);
	}

}
