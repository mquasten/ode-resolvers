package de.mq.odesolver.solve.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.mq.odesolver.solve.OdeSolverService.Algorithm;

class OdeTest {
	
	private static final int STEPS = 10000;
	private static final int STOP = 10;
	private static final int START = 0;
	private static final double[] Y0 = new double[] {0,1};
	private static final String ODE2 = "y[1]/y[0]+x";
	private final Ode ode = newOde2();

	private Ode newOde2() {
		return new Ode(ODE2, Algorithm.RungeKutta4ndOrder, Y0, START, STOP, STEPS );
	}
	
	private Ode newOde1() {
		return new Ode("y[0]+x", Algorithm.RungeKutta4ndOrder, new double[] {0}, START, STOP, STEPS );
	}
	
	@Test
	void ode() {
		assertEquals(ODE2, ode.ode());
	}
	
	@Test
	void beautifiedOdeOrder2() {
		assertEquals("y''=y'/y+x", ode.beautifiedOde());
	}
	
	@Test
	void beautifiedOdeOrder1() {
		assertEquals("y'=y+x", newOde1().beautifiedOde());
	}
	
	@Test
	void algorithm() {
		assertEquals(Algorithm.RungeKutta4ndOrder, ode.algorithm());
	}

	@Test
	void y() {
		assertEquals(Y0, ode.y());
	}
	
	@Test
	void checkOrder() {
		assertTrue(ode.checkOrder(2));
		assertFalse(ode.checkOrder(1));
	}
	@ParameterizedTest
	@ValueSource(ints = {0,-1})
	void ckeckOrderWrongOrder(final int order) {
		assertThrows(IllegalArgumentException.class, () -> ode.checkOrder(order));
	}
	
	@Test
	void start() {
		assertEquals(START, ode.start());
	}
	
	
	@Test
	void stop() {
		assertEquals(STOP, ode.stop());
	}
	@Test
	void checkStartBeforeStop() {
		assertTrue(ode.checkStartBeforeStop());
	}
	
	@Test
	void checkStartBeforeStopFalse() {
		assertFalse( new Ode(ODE2, Algorithm.RungeKutta4ndOrder, Y0, START, START, STEPS ).checkStartBeforeStop());
	}
	
	@Test
	void steps() {
		assertEquals(STEPS, ode.steps());
	}
	@Test
	void odeEmpty() {
		assertThrows(IllegalArgumentException.class, () -> new Ode(null, Algorithm.RungeKutta4ndOrder, Y0, START, STOP, STEPS ));
	}
	
	@Test
	void AlgorithmNull() {
		assertThrows(IllegalArgumentException.class, () -> new Ode(ODE2, null, Y0, START, STOP, STEPS ));
	}
	
	@Test
	void yNull() {
		assertThrows(IllegalArgumentException.class, () -> new Ode(ODE2, Algorithm.RungeKutta4ndOrder, null, START, STOP, STEPS ));
	}
	
	@Test
	void yLength0() {
		assertThrows(IllegalArgumentException.class, () -> new Ode(ODE2, Algorithm.RungeKutta4ndOrder, new double[] {}, START, STOP, STEPS ));
	}
	
	@Test
	void steps0() {
		assertThrows(IllegalArgumentException.class, () -> new Ode(ODE2, Algorithm.RungeKutta4ndOrder, Y0, START, STOP, 0 ));
	}
}
