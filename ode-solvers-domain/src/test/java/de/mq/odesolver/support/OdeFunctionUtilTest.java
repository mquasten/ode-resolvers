package de.mq.odesolver.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.script.Invocable;

import org.junit.jupiter.api.Test;

class OdeFunctionUtilTest {

	private final OdeFunctionUtil odeFunctionUtil = new OdeFunctionUtil();

	@Test
	void berechnehoechsteAbleitung() {
		final Invocable invocable = odeFunctionUtil.prepareFunction("y[1]+y[0]+x");

		assertEquals(6, odeFunctionUtil.invokeFunction(invocable, new double[] { 1, 2 }, 3));
	}

	@Test
	void prepareFunctionException() {
		assertThrows(IllegalStateException.class, () -> odeFunctionUtil.prepareFunction("y'+y+x"));

	}

	@Test
	void invokeFunctionException() {
		final Invocable invocable = odeFunctionUtil.prepareFunction("y[1]+y[0]+t");
		assertThrows(IllegalStateException.class,
				() -> odeFunctionUtil.invokeFunction(invocable, new double[] { 1, 2 }, 3));
	}

	@Test
	void invokeFunctionNaN() {
		final Invocable invocable = odeFunctionUtil.prepareFunction("y[1]+y[0]+x");
		assertThrows(IllegalArgumentException.class,
				() -> odeFunctionUtil.invokeFunction(invocable, new double[] { 1 }, 3));
	}

	@Test
	void invokeFunctionInvinit() {
		final Invocable invocable = odeFunctionUtil.prepareFunction("y[0]/x");
		assertThrows(IllegalArgumentException.class,
				() -> odeFunctionUtil.invokeFunction(invocable, new double[] { 1 }, 0));
	}

	@Test
	void invokeFunctionReturnValueIsNotANumber() {
		final Invocable invocable = odeFunctionUtil.prepareFunction("y+ x");
		assertThrows(IllegalStateException.class,
				() -> odeFunctionUtil.invokeFunction(invocable, new double[] { 0 }, 1));
	}
	
	@Test
	void invokeFunctionReturnNull() {
		final Invocable invocable = odeFunctionUtil.prepareFunction("y[1]");
		
		assertThrows(IllegalArgumentException.class, ()->odeFunctionUtil.invokeFunction(invocable, new double[] { 0 }, 1));
	}

}
