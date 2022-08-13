package de.mq.odesolver.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import de.mq.odesolver.OdeResolver;
import de.mq.odesolver.impl.OdeResolverService.Algorithm;

class OdeResolverServiceImplTest {

	private static final String ODE_STING_COMPILE_ERROR = "y[0] x";
	private static final String ODE_STRING = "y[0]-x";
	final OdeResolverService odeResolverService = new OdeResolverServiceImpl();

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
	void solve() {
		final OdeResolver odeResolver=odeResolverService.odeResolver(Algorithm.RungeKutta4ndOrder, ODE_STRING);
		assertEquals(101, odeResolverService.solve(odeResolver, newOdeResult(), 1, 100).size());
	}

	private OdeResultImpl newOdeResult() {
		return new OdeResultImpl(OdeResultImpl.doubleArray(1), 0);
	}	
	
	@Test
	void validate() {
		final OdeResolver odeResolver=odeResolverService.odeResolver(Algorithm.RungeKutta4ndOrder, ODE_STRING);
		odeResolverService.validate(odeResolver, newOdeResult());
	}
	
	@Test
	void validateInvalid() {
		final OdeResolver odeResolver=odeResolverService.odeResolver(Algorithm.RungeKutta4ndOrder, "y[0]/x");
		assertThrows(IllegalArgumentException.class, ()->odeResolverService.validate(odeResolver, newOdeResult()));
	}

}
