package de.mq.odesolver.impl;

import org.junit.jupiter.api.BeforeEach;

import de.mq.odesolver.OdeResolver;

class TestOdeResolverImplWithRungeKutta2CalculatorImplAndLamdas extends AbstractTestOdeResolver {
	
	@BeforeEach
	void init() {
		tolerances.put(AbstractTestOdeResolver.MaxTol.ExamplePapulaSecondOrderErrorEstimate, 1e-1);
		tolerances.put(AbstractTestOdeResolver.MaxTol.ErrorEstimaions, 6e-4);
		tolerances.put(AbstractTestOdeResolver.MaxTol.ExamplePapulaSecondOrder, 2e-6);
		tolerances.put(AbstractTestOdeResolver.MaxTol.SqrtYPlusYStart0, 6e-6);
		tolerances.put(AbstractTestOdeResolver.MaxTol.SqrtYPlusYStart1, 3e-6);
		
		expectedResults.put(AbstractTestOdeResolver.Result.ExamplePapulaFirstOrder, new double[] {0, -0.005, -0.021025, -0.049233});
		expectedResults.put( AbstractTestOdeResolver.Result.ExamplePapulaSecondOrderY, new double[] {0,0.36000, 0.66600	});
		expectedResults.put( AbstractTestOdeResolver.Result.ExamplePapulaSecondOrderDerivative, new double[] {4, 3.3400, 2.8861	});
	}

	@Override
	OdeResolver newOdeResolver(TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta2CalculatorImpl(testDgl.odeFunction()));
	}

}
