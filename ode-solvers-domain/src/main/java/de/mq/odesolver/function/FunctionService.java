package de.mq.odesolver.function;

import java.util.List;

import de.mq.odesolver.Result;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

public interface FunctionService {

	FunctionSolver functionSolver(final Language language, final String function);
	
	List<Result> solve(final Function Function);

	double validate(final Language language, final String function, final double x0, final double k[]);
	
	double validate(final Function function);

}