package de.mq.odesolver.impl;

import org.junit.jupiter.api.BeforeEach;

import de.mq.odesolver.OdeResolver;

class TestOdeResolverImplWithEulerCalculatorImplAndLamdas extends AbstractTestOdeResolver {
	
	@BeforeEach
	void init() {
		properties.put(AbstractTestOdeResolver.TestProperties.EndExamplePapulaFirstOrder, 0.2d);
		properties.put(AbstractTestOdeResolver.TestProperties.StepsExamplePapulaFirstOrder, 4d);
		properties.put(AbstractTestOdeResolver.TestProperties.MaxTolErrorEstimaions, 2e-3);
		properties.put(AbstractTestOdeResolver.TestProperties.MaxTolSqrtYPlusYStart0,3e-3);
		properties.put(AbstractTestOdeResolver.TestProperties.MaxTolSqrtYPlusYStart1, 6e-3);
		properties.put(AbstractTestOdeResolver.TestProperties.MaxTolExamplePapulaSecondOrder, 3e-3);
		properties.put(AbstractTestOdeResolver.TestProperties.MaxTolExamplePapulaSecondOrderErrorEstimate, 5e-1);
		expectedResults.put(AbstractTestOdeResolver.Result.ExamplePapulaFirstOrder, new double[] {0, 0, -0.0025, -0.007625, -0.015506});
		expectedResults.put( AbstractTestOdeResolver.Result.ExamplePapulaSecondOrderY, new double[] {0,0.4,0.72	});
		expectedResults.put( AbstractTestOdeResolver.Result.ExamplePapulaSecondOrderDerivative, new double[] {4, 3.2, 2.68	});
		
	}

	@Override
	OdeResolver newOdeResolver(TestDgl testDgl) {
		return new OdeSolverImpl(new EulerCalculatorImpl(testDgl.odeFunction()));
	}

}
