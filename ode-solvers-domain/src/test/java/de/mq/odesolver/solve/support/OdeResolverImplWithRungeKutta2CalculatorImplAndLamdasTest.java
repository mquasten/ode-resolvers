package de.mq.odesolver.solve.support;

import org.junit.jupiter.api.BeforeEach;

import de.mq.odesolver.solve.OdeSolver;

class OdeResolverImplWithRungeKutta2CalculatorImplAndLamdasTest extends AbstractTestOdeResolver {
	
	@BeforeEach
	void init() {
		properties.put(AbstractTestOdeResolver.TestProperties.MaxTolExamplePapulaSecondOrderErrorEstimate, 1e-1);
		properties.put(AbstractTestOdeResolver.TestProperties.MaxTolErrorEstimaions, 6e-4);
		properties.put(AbstractTestOdeResolver.TestProperties.MaxTolExamplePapulaSecondOrder, 2e-6);
		properties.put(AbstractTestOdeResolver.TestProperties.MaxTolSqrtYPlusYStart0, 6e-6);
		properties.put(AbstractTestOdeResolver.TestProperties.MaxTolSqrtYPlusYStart1, 3e-6);
		
		expectedResults.put(AbstractTestOdeResolver.Result.ExamplePapulaFirstOrder, new double[] {0, -0.005, -0.021025, -0.049233});
		expectedResults.put( AbstractTestOdeResolver.Result.ExamplePapulaSecondOrderY, new double[] {0,0.36000, 0.66600	});
		expectedResults.put( AbstractTestOdeResolver.Result.ExamplePapulaSecondOrderDerivative, new double[] {4, 3.3400, 2.8861	});
	}

	@Override
	OdeSolver newOdeResolver(TestDgl testDgl) {
		return new OdeSolverImpl(new RungeKutta2CalculatorImpl(testDgl.odeFunction()));
	}

}
