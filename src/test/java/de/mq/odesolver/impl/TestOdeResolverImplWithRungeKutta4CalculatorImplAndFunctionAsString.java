package de.mq.odesolver.impl;

import de.mq.odesolver.OdeResolver;

public class TestOdeResolverImplWithRungeKutta4CalculatorImplAndFunctionAsString extends AbstractTestOdeResolver {

	@Override
	OdeResolver newOdeResolver(final TestDgl testDgl) {
		return new OdeResolverImpl(new RungeKutta4CalculatorImpl(testDgl.functionAsString()));
	}
}
