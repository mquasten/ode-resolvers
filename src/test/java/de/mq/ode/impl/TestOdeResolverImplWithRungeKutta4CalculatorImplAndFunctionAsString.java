package de.mq.ode.impl;

import de.mq.ode.OdeResolver;

public class TestOdeResolverImplWithRungeKutta4CalculatorImplAndFunctionAsString extends AbstractTestOdeResolver {

	@Override
	OdeResolver newOdeResolver(final TestDgl testDgl) {
		return new OdeResolverImpl(new RungeKutta4CalculatorImpl(testDgl.functionAsString()));
	}
}
