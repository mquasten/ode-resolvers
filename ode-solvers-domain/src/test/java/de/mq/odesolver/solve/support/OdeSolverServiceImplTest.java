package de.mq.odesolver.solve.support;

import static de.mq.odesolver.support.OdeFunctionUtilFactory.newOdeFunctionUtil;
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
	void odeResolver(final Language language) {
		final OdeSolverService odeResolverService = new OdeSolverServiceImpl(newOdeFunctionUtil(language));
		Arrays.asList(Algorithm.values()).forEach(algorithm -> odeResolverService.odeSolver(algorithm, ODE_STRING));
	}

	@ParameterizedTest
	@EnumSource
	void odeResolverException(final Language language) {
		final OdeSolverService odeResolverService = new OdeSolverServiceImpl(newOdeFunctionUtil(language));
		assertThrows(IllegalStateException.class,
				() -> odeResolverService.odeSolver(Algorithm.EulerPolygonal, ODE_STING_COMPILE_ERROR));
	}

	@ParameterizedTest
	@EnumSource
	void validate(final Language language) {
		final OdeSolverService odeResolverService = new OdeSolverServiceImpl(newOdeFunctionUtil(language));
		odeResolverService.validateRightSide(ODE_STRING, Y, 0);
	}

	@ParameterizedTest
	@EnumSource
	void validateInvalid(final Language language) {
		final OdeSolverService odeResolverService = new OdeSolverServiceImpl(newOdeFunctionUtil(language));
		assertThrows(IllegalArgumentException.class, () -> odeResolverService.validateRightSide("y[0]/x", Y, 0));
	}

}
