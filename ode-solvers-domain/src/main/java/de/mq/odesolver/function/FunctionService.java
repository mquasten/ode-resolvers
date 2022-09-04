package de.mq.odesolver.function;

import de.mq.odesolver.support.OdeFunctionUtil.Language;

public interface FunctionService {

	FunctionSolver functionSolver(final Language language, final String function);

	double validateValue(final Language language, final String function, final double x0, final double k[]);

}