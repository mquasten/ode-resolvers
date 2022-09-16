package de.mq.odesolver.solve.support;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.mq.odesolver.solve.OdeSolverService.Algorithm;

class OdeModelTest {
	
	private static final String STEPS = "1000";
	private static final String STOP = "1";
	private static final String START = "-1";
	private static final String Y = "0,1";
	private static final String ODE = "y[0]+x";
	private static final int ORDER = 2;
	private final OdeModel odeModel= new OdeModel();
	
	@Test
	void order() {
		assertNull(odeModel.getOrder());
		odeModel.setOrder(ORDER);
		assertEquals(ORDER, odeModel.getOrder());
	}
	@Test
	void ode() {
		assertNull(odeModel.getOde());
		odeModel.setOde(ODE);
		assertEquals(ODE, odeModel.getOde());
	}
	@Test
	void solver() {
		assertNull(odeModel.getSolver());
		odeModel.setSolver(Algorithm.RungeKutta4thOrder.name());
		assertEquals(Algorithm.RungeKutta4thOrder.name(), odeModel.getSolver());
	}
	@Test
	void  y() {
		assertNull(odeModel.getY());
		odeModel.setY(Y);
		assertEquals(Y, odeModel.getY());
	}
	@Test
	void start() {
		assertNull(odeModel.getStart());
		odeModel.setStart(START);
		assertEquals(START, odeModel.getStart());
	}
	@Test
	void stop() {
		assertNull(odeModel.getStop());
		odeModel.setStop(STOP);
		assertEquals(STOP, odeModel.getStop());
	}
	
	@Test
	void steps() {
		assertNull(odeModel.getSteps());
		odeModel.setSteps(STEPS);
		assertEquals(STEPS, odeModel.getSteps());
	}
	

}
