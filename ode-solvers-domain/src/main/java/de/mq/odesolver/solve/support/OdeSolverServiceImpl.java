package de.mq.odesolver.solve.support;

import java.lang.reflect.Constructor;
import java.util.Map;

import javax.script.Invocable;

import de.mq.odesolver.solve.OdeResultCalculator;
import de.mq.odesolver.solve.OdeSolver;
import de.mq.odesolver.solve.OdeSolverService;
import de.mq.odesolver.support.OdeFunctionUtil;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

class OdeSolverServiceImpl implements OdeSolverService {

	private final Map<Algorithm, Class<? extends OdeResultCalculator>> solvers = Map.of(Algorithm.EulerPolygonal, EulerCalculatorImpl.class, Algorithm.RungeKutta2ndOrder,
			RungeKutta2CalculatorImpl.class, Algorithm.RungeKutta4thOrder, RungeKutta4CalculatorImpl.class);

	@Override
	public final OdeSolver odeSolver(final Language language, final Algorithm algorithm, final String function) {
		try {
			final OdeResultCalculator odeResultCalculator = solvers.get(algorithm).getDeclaredConstructor(OdeFunctionUtil.class, String.class).newInstance(newOdeFunctionUtil(language), function);
			return new OdeSolverImpl(odeResultCalculator);
		} catch (final Exception exception) {
			throw exception(exception);
		}

	}

	private RuntimeException exception(final Exception exception) {
		if (exception instanceof RuntimeException) {
			 return  (RuntimeException) exception;
		}
		return new IllegalStateException(exception.getCause());
	}

	private OdeFunctionUtil newOdeFunctionUtil(final Language language) throws Exception {
		@SuppressWarnings("unchecked")
		final Constructor<? extends OdeFunctionUtil> constructor = (Constructor<? extends OdeFunctionUtil>) Class.forName("de.mq.odesolver.support.OdeFunctionUtilImpl")
				.getDeclaredConstructor(Language.class);
		constructor.setAccessible(true);
		return constructor.newInstance(language);
	}

	@Override
	public final double validateRightSide(final Language language, final String function, final double y0[], final double x0) {

		try {
			final OdeFunctionUtil odeFunctionUtil = newOdeFunctionUtil(language);
			final Invocable invocable = odeFunctionUtil.prepareFunction(function);
			return odeFunctionUtil.invokeFunction(invocable, y0, x0);
		} catch (final Exception exception) {
			throw exception(exception);
		}

	}

}
