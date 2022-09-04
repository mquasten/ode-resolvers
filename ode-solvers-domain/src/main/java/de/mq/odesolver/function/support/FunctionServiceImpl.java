package de.mq.odesolver.function.support;

import java.lang.reflect.Constructor;

import javax.script.Invocable;

import de.mq.odesolver.function.FunctionService;
import de.mq.odesolver.function.FunctionSolver;
import de.mq.odesolver.support.OdeFunctionUtil;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

class FunctionServiceImpl implements FunctionService {

	@Override
	public final FunctionSolver functionSolver(final Language language, final String function) {

		try {
			final OdeFunctionUtil odeFunctionUtil = newOdeFunctionUtil(language);
			return new FunctionSolverImpl(odeFunctionUtil, function);

		} catch (final Exception exception) {
			throw exception(exception);
		}

	}

	@Override
	public final double validateValue(final Language language, final String function, final double x0, final double k[]) {

		try {
			final OdeFunctionUtil odeFunctionUtil = newOdeFunctionUtil(language);
			final Invocable invocable = odeFunctionUtil.prepareFunction(function);
			return odeFunctionUtil.invokeFunction(invocable, k, x0);
		} catch (final Exception exception) {
			throw exception(exception);
		}
	}

	private RuntimeException exception(final Exception exception) {
		if (exception instanceof RuntimeException) {
			return (RuntimeException) exception;
		}
		return new IllegalStateException(exception.getCause());
	}

	private OdeFunctionUtil newOdeFunctionUtil(final Language language) throws Exception {
		@SuppressWarnings("unchecked")
		final Constructor<? extends OdeFunctionUtil> constructor = (Constructor<? extends OdeFunctionUtil>) Class.forName("de.mq.odesolver.support.OdeFunctionUtilImpl")
				.getDeclaredConstructor(Language.class, String.class);
		constructor.setAccessible(true);
		return constructor.newInstance(language, "k");
	}

}
