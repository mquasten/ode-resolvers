package de.mq.odesolver.solve.support;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.mq.odesolver.solve.OdeSolverService;
import de.mq.odesolver.solve.OdeSolverService.Algorithm;
import de.mq.odesolver.support.OdeFunctionUtil.Language;


class OdeSolverServiceImplTest {

	private static final double[] Y = OdeResultImpl.doubleArray(1);
	private static final String ODE_STING_COMPILE_ERROR = "y[0] x";
	private static final String ODE_STRING = "y[0]-x";

	@ParameterizedTest
	@EnumSource
	void odeSolver(final Language language) {
		final OdeSolverService odeResolverService = new OdeSolverServiceImpl();
		Arrays.asList(Algorithm.values()).forEach(algorithm -> odeResolverService.odeSolver(language, algorithm, ODE_STRING));
	}

	@ParameterizedTest
	@EnumSource
	void odeResolverException(final Language language) {
		final OdeSolverService odeResolverService = new OdeSolverServiceImpl();
		assertThrows(IllegalStateException.class,
				() -> odeResolverService.odeSolver(language, Algorithm.EulerPolygonal, ODE_STING_COMPILE_ERROR));
	}

	@ParameterizedTest
	@EnumSource
	void validate(final Language language) {
		final OdeSolverService odeResolverService = new OdeSolverServiceImpl();
		odeResolverService.validateRightSide(language,ODE_STRING, Y, 0);
	}

	@ParameterizedTest
	@EnumSource
	void validateInvalid(final Language language) {
		final OdeSolverService odeResolverService = new OdeSolverServiceImpl();
		assertThrows(IllegalArgumentException.class, () -> odeResolverService.validateRightSide(language, "y[0]/x", Y, 0));
	}
	

}
