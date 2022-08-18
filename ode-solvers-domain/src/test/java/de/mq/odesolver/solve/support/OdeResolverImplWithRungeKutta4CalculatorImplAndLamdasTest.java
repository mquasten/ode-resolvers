package de.mq.odesolver.solve.support;

import de.mq.odesolver.solve.OdeSolver;


class OdeResolverImplWithRungeKutta4CalculatorImplAndLamdasTest extends AbstractTestOdeResolver {

	@Override
	OdeSolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta4CalculatorImpl(testDgl.odeFunction()));
	}

}
