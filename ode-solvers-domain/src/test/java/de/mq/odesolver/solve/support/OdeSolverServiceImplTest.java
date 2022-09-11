package de.mq.odesolver.solve.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.mq.odesolver.solve.Ode;
import de.mq.odesolver.solve.OdeSolverService;
import de.mq.odesolver.solve.OdeSolverService.Algorithm;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

class OdeSolverServiceImplTest {

	private static final int STEPS = 1000;
	private static final int STOP = 10;
	private static final int START = -10;
	private static final double[] Y = OdeResultImpl.doubleArray(1);
	private static final String ODE_STING_COMPILE_ERROR = "y[0] x";
	private static final String ODE_STRING = "y[0]-x";

	private final OdeSolverService odeSolverService = new OdeSolverServiceImpl();

	@ParameterizedTest
	@EnumSource
	void odeSolver(final Language language) {
		Arrays.asList(Algorithm.values()).forEach(algorithm -> odeSolverService.odeSolver(language, algorithm, ODE_STRING));
	}

	@ParameterizedTest
	@EnumSource
	void odeResolverException(final Language language) {
		assertThrows(IllegalStateException.class, () -> odeSolverService.odeSolver(language, Algorithm.EulerPolygonal, ODE_STING_COMPILE_ERROR));
	}

	@ParameterizedTest
	@EnumSource
	void validateRightSide(final Language language) {
		assertEquals(11d, odeSolverService.validateRightSide(language, ODE_STRING, Y, START));
	}

	@ParameterizedTest
	@EnumSource
	void validateRightSideWithOde(final Language language) {
		final Ode ode = newOde(language);
		assertEquals(11d, odeSolverService.validateRightSide(ode));
	}

	private OdeImpl newOde(final Language language) {
		return new OdeImpl(language, ODE_STRING, Algorithm.RungeKutta4thOrder, Y, START, STOP, STEPS);
	}

	@ParameterizedTest
	@EnumSource
	void validateInvalid(final Language language) {
		assertThrows(IllegalArgumentException.class, () -> odeSolverService.validateRightSide(language, "y[0]/x", Y, 0));
	}

	@ParameterizedTest
	@EnumSource
	void solve(final Language language) {
		final Ode ode = newOde(language);
		assertEquals(1 + ode.steps(), odeSolverService.solve(ode).size());
	}
	
	@Test
	void algorithms() {
		final var orders= Map.of(Algorithm.EulerPolygonal, 1, Algorithm.RungeKutta2ndOrder, 2, Algorithm.RungeKutta4thOrder, 4);
		Arrays.asList(Algorithm.values()).forEach(algorithm -> assertEquals(orders.get(algorithm), algorithm.order()));
	}

}
