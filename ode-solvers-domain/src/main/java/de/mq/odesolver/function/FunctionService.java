package de.mq.odesolver.function;

public interface FunctionService {

	FunctionSolver functionSolver(final String function);

	double validateValue(final String function, final double x0, final double k[]);

}