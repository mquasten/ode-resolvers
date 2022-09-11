package de.mq.odesolver.function.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.mq.odesolver.function.Function;
import de.mq.odesolver.support.OdeFunctionUtil.Language;

class FunctionImplTest {

	private static final int STEPS = 10000;
	private static final int STOP = 10;
	private static final int START = 0;
	private static final double[] K = new double[] { 0, 1 };
	private static final String FUNCTION = "k[1]*Math.pow(x,2)+k[0]*x";

	private final Function function = newFunction();

	private Function newFunction() {
		return new FunctionImpl(Language.Nashorn, FUNCTION, START, STOP, STEPS, K);
	}

	@Test
	void ode() {
		assertEquals(FUNCTION, function.function());
	}

	@Test
	void language() {
		assertEquals(Language.Nashorn, function.language());
	}

	@Test
	void k() {
		assertEquals(K, function.k());
	}

	@Test
	void start() {
		assertEquals(START, function.start());
	}

	@Test
	void stop() {
		assertEquals(STOP, function.stop());
	}

	@Test
	void checkStartBeforeStop() {
		assertTrue(function.checkStartBeforeStop());
	}

	@Test
	void checkStartBeforeStopFalse() {
		assertFalse(new FunctionImpl(Language.Nashorn, FUNCTION, START, START, STEPS, K).checkStartBeforeStop());
	}

	@Test
	void steps() {
		assertEquals(STEPS, function.steps());
	}

	@Test
	void languageEmpty() {
		assertThrows(NullPointerException.class, () -> new FunctionImpl(null, FUNCTION, START, STOP, STEPS, K));
	}

	@Test
	void functionEmpty() {
		assertThrows(NullPointerException.class, () -> new FunctionImpl(Language.Nashorn, null, START, STOP, STEPS, K));
	}

	@Test
	void steps0() {
		assertThrows(IllegalArgumentException.class, () -> new FunctionImpl(Language.Nashorn, FUNCTION, START, STOP, 0, K));
	}

}