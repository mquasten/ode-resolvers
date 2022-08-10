package de.mq.odesolver.impl;

import de.mq.odesolver.OdeResolver;

public class TestOdeResolverImplWithRungeKutta2CalculatorImplAndFunctionAsString extends TestOdeResolverImplWithRungeKutta2CalculatorImplAndLamdas {
	
	
	@Override
	OdeResolver newOdeResolver(final TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta2CalculatorImpl(testDgl.functionAsString()));
	}


}
