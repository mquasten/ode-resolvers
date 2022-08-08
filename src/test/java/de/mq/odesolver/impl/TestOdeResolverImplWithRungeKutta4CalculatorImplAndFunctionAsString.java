package de.mq.odesolver.impl;

import de.mq.odesolver.OdeResolver;

public class TestOdeResolverImplWithRungeKutta4CalculatorImplAndFunctionAsString extends AbstractTestOdeResolver {

	@Override
	OdeResolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta4CalculatorImpl(testDgl.functionAsString()));
	}
}
