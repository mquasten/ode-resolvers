package de.mq.odesolver.solve.support;

import de.mq.odesolver.solve.OdeSolver;

public class OdeResolverImplWithRungeKutta2CalculatorImplAndFunctionAsStringTest extends OdeResolverImplWithRungeKutta2CalculatorImplAndLamdasTest {
	
	
	@Override
	OdeSolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta2CalculatorImpl(testDgl.functionAsString()));
	}


}
