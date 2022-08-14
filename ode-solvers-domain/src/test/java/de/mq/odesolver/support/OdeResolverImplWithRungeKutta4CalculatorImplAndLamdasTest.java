package de.mq.odesolver.support;

import de.mq.odesolver.OdeSolver;


class OdeResolverImplWithRungeKutta4CalculatorImplAndLamdasTest extends AbstractTestOdeResolver {

	@Override
	OdeSolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta4CalculatorImpl(testDgl.odeFunction()));
	}

}
