package de.mq.odesolver.support;

import de.mq.odesolver.OdeResolver;


class OdeResolverImplWithRungeKutta4CalculatorImplAndLamdasTest extends AbstractTestOdeResolver {

	@Override
	OdeResolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta4CalculatorImpl(testDgl.odeFunction()));
	}

}
