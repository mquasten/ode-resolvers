package de.mq.odesolver.impl;

import de.mq.odesolver.OdeResolver;


class TestOdeResolverImplWithRungeKutta4CalculatorImplAndLamdas extends AbstractTestOdeResolver {

	@Override
	OdeResolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta4CalculatorImpl(testDgl.odeFunction()));
	}

}
