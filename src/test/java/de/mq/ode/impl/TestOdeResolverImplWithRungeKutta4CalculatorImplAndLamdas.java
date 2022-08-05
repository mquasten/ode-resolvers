package de.mq.ode.impl;

import de.mq.ode.OdeResolver;

class TestOdeResolverImplWithRungeKutta4CalculatorImplAndLamdas extends AbstractTestOdeResolver {

	@Override
	OdeResolver newOdeResolver(final TestDgl testDgl) {
		return new OdeResolverImpl(new RungeKutta4CalculatorImpl(testDgl.odeFunction()));
	}

}
