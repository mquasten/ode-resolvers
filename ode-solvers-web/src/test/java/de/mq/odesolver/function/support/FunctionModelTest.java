package de.mq.odesolver.function.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class FunctionModelTest {
	
	private final FunctionModel functionModel = new FunctionModel();
	
	@Test
	void function() {
		final var function = "X**2";
		assertNull(functionModel.getFunction());
		functionModel.setFunction(function);
		assertEquals(function, functionModel.getFunction());
	}
	@Test
	void k() {
		final var k="1,2";
		assertNull(functionModel.getK());
		functionModel.setK(k);
		assertEquals(k, functionModel.getK());
	}
	@Test
	void start() {
		final var start = "1";
		assertNull(functionModel.getStart());
		functionModel.setStart(start);
		assertEquals(start, functionModel.getStart());
	}
	@Test
	void stop() {
		final var stop= "2"; 
		assertNull(functionModel.getStop());
		functionModel.setStop(stop);
		assertEquals(stop, functionModel.getStop());
	}
	@Test
	void steps() {
		final var steps="1000";
		assertNull(functionModel.getSteps());
		functionModel.setSteps(steps);
		assertEquals(steps, functionModel.getSteps());
		
	}

}
