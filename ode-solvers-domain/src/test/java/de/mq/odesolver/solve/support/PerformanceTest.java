package de.mq.odesolver.solve.support;

import static de.mq.odesolver.support.OdeFunctionUtilFactory.newOdeFunctionUtil;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.mq.odesolver.solve.OdeSolver;
import de.mq.odesolver.support.OdeFunctionUtil;
import de.mq.odesolver.support.OdeFunctionUtil.Language;


class PerformanceTest {
	
	@ParameterizedTest
	@EnumSource
	void run(final Language language) {
		
		final OdeFunctionUtil odeFunctionUtil = newOdeFunctionUtil(language);
		final OdeSolver odeSolver = new OdeSolverImpl(new RungeKutta2CalculatorImpl(odeFunctionUtil, "y[0]+x"));
		final long t1 = System.currentTimeMillis();
		odeSolver.solve( new double[] {1}, 0, 1, 1000000);
		assertTrue( (System.currentTimeMillis() - t1) / 1000  < 5d);
		
	}

}
