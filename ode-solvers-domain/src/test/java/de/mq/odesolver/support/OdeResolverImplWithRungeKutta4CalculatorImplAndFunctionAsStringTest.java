package de.mq.odesolver.support;

import de.mq.odesolver.OdeResolver;

public class OdeResolverImplWithRungeKutta4CalculatorImplAndFunctionAsStringTest extends AbstractTestOdeResolver {

	@Override
	OdeResolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta4CalculatorImpl(testDgl.functionAsString()));
	}
}
