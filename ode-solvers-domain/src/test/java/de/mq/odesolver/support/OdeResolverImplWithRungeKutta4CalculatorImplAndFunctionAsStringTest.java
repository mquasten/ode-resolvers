package de.mq.odesolver.support;

import de.mq.odesolver.OdeSolver;

public class OdeResolverImplWithRungeKutta4CalculatorImplAndFunctionAsStringTest extends AbstractTestOdeResolver {

	@Override
	OdeSolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta4CalculatorImpl(testDgl.functionAsString()));
	}
}
