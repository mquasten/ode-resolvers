package de.mq.odesolver.solve.support;

import de.mq.odesolver.solve.OdeSolver;

public class OdeResolverImplWithRungeKutta4CalculatorImplAndFunctionAsStringTest extends AbstractTestOdeResolver {

	@Override
	OdeSolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta4CalculatorImpl(testDgl.functionAsString()));
	}
}
