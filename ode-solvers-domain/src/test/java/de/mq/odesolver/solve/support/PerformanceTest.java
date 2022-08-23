package de.mq.odesolver.solve.support;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.mq.odesolver.solve.OdeSolver;
import de.mq.odesolver.solve.support.OdeFunctionUtil.Language;

class PerformanceTest {
	
	@ParameterizedTest
	@EnumSource
	void run(final Language language) {
		final OdeFunctionUtil odeFunctionUtil = new OdeFunctionUtil(language);
		final OdeSolver odeSolver = new OdeSolverImpl(new RungeKutta2CalculatorImpl(odeFunctionUtil, "y[0]+x"));
		final long t1 = System.currentTimeMillis();
		odeSolver.solve( new double[] {1}, 0, 1, 1000000);
		System.out.println(language+ ": " + (System.currentTimeMillis() - t1));
		
	}

}
