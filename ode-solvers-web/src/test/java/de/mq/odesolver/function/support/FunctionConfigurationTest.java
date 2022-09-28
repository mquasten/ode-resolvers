package de.mq.odesolver.function.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.mq.odesolver.support.OdeFunctionUtil.Language;

class FunctionConfigurationTest {

	private static final int STEPS = 1000;
	private static final double[] K = new double[] { 1, 2 };
	private static final String FUNCTION = "1/2*Math.pow(x,4)+k[0]*Math.pow(x,2)+k[1]*Math.pow(x,4)";
	private final FunctionConfiguration functionConfiguration = new FunctionConfiguration();

	@Test
	void functionService() {
		final var functionService = functionConfiguration.functionService();
		assertEquals(3.5d, functionService.validate(Language.Nashorn, FUNCTION, 1, K));
		assertEquals(1 + STEPS, functionService.functionSolver(Language.Nashorn, FUNCTION).solve(K, 0, 2, STEPS).size());
	}

}
