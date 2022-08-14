package de.mq.odesolver.support;

import de.mq.odesolver.OdeResolver;

public class OdeResolverImplWithRungeKutta2CalculatorImplAndFunctionAsStringTest extends OdeResolverImplWithRungeKutta2CalculatorImplAndLamdasTest {
	
	
	@Override
	OdeResolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta2CalculatorImpl(testDgl.functionAsString()));
	}


}
