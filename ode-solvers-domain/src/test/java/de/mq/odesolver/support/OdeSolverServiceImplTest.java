package de.mq.odesolver.support;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import de.mq.odesolver.OdeSolverService;
import de.mq.odesolver.OdeSolverService.Algorithm;

class OdeSolverServiceImplTest {

	private static final double[] Y = OdeResultImpl.doubleArray(1);
	private static final String ODE_STING_COMPILE_ERROR = "y[0] x";
	private static final String ODE_STRING = "y[0]-x";
	final OdeSolverService odeResolverService = new OdeSolverServiceImpl(new OdeFunctionUtil());

	@Test
	void odeResolver() {
		Arrays.asList(Algorithm.values())
				.forEach(algorithm -> assertNotNull(odeResolverService.odeResolver(algorithm, ODE_STRING)));
	}

	@Test
	void odeResolverException() {
		assertThrows(IllegalStateException.class,
				() -> odeResolverService.odeResolver(Algorithm.EulerPolygonal, ODE_STING_COMPILE_ERROR));
	}
	

	@Test
	void validate() {
		odeResolverService.validateRightSide(ODE_STRING, Y, 0);
	}
	
	@Test
	void validateInvalid() {
		assertThrows(IllegalArgumentException.class, ()->odeResolverService.validateRightSide( "y[0]/x", Y, 0));
	}

}
