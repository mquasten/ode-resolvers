package de.mq.odesolver.solve.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.mq.odesolver.solve.OdeSolverService.Algorithm;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

class OdeSolverConfigurationTest {
	
	private static final int RESULT_SIZE = 1000;
	private final OdeSolverConfiguration odeSolverConfiguration = new OdeSolverConfiguration();
	
	
	@Test
	void odeSolverService() {
		final var  odeSolverService =  odeSolverConfiguration.odeSolverService();
		assertEquals(1+RESULT_SIZE, odeSolverService.odeSolver(Language.Groovy, Algorithm.RungeKutta4thOrder, "y[0] + x").solve(new double[] {1}, 0, 1, RESULT_SIZE).size());
	}

}
