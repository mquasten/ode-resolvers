package de.mq.odesolver.solve.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.script.Invocable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.mq.odesolver.solve.support.OdeFunctionUtil.Language;

class OdeFunctionUtilTest {

	@ParameterizedTest
	@EnumSource
	void berechnehoechsteAbleitung(final Language language) {
		final OdeFunctionUtil odeFunctionUtil = new OdeFunctionUtil(language);
		final Invocable invocable = odeFunctionUtil.prepareFunction("y[1]+y[0]+x");

		assertEquals(6, odeFunctionUtil.invokeFunction(invocable, new double[] { 1, 2 }, 3));
	}

	@ParameterizedTest
	@EnumSource
	void prepareFunctionException(final Language language) {
		final OdeFunctionUtil odeFunctionUtil = new OdeFunctionUtil(language);
		assertThrows(IllegalStateException.class, () -> odeFunctionUtil.prepareFunction("y'+y+x"));

	}

	@ParameterizedTest
	@EnumSource
	void invokeFunctionException(final Language language) {
		final OdeFunctionUtil odeFunctionUtil = new OdeFunctionUtil(language);
		final Invocable invocable = odeFunctionUtil.prepareFunction("y[1]+y[0]+t");
		assertThrows(IllegalStateException.class,
				() -> odeFunctionUtil.invokeFunction(invocable, new double[] { 1, 2 }, 3));
	}

	@ParameterizedTest
	@EnumSource
	void invokeFunctionNaN(final Language language) {
		final OdeFunctionUtil odeFunctionUtil = new OdeFunctionUtil(language);
		final Invocable invocable = odeFunctionUtil.prepareFunction("y[1]+y[0]+x");
		assertThrows(IllegalStateException.class,
				() -> odeFunctionUtil.invokeFunction(invocable, new double[] { 1 }, 3));
	}

	@ParameterizedTest
	@EnumSource
	void invokeFunctionInvinit(final Language language) {
		final OdeFunctionUtil odeFunctionUtil = new OdeFunctionUtil(language);
		final Invocable invocable = odeFunctionUtil.prepareFunction("y[0]/x");
		assertThrows(IllegalArgumentException.class,
				() -> odeFunctionUtil.invokeFunction(invocable, new double[] { 1 }, 0));
	}

	@ParameterizedTest
	@EnumSource
	void invokeFunctionReturnValueIsNotANumber(final Language language) {
		final OdeFunctionUtil odeFunctionUtil = new OdeFunctionUtil(language);
		final Invocable invocable = odeFunctionUtil.prepareFunction("y+ x");
		assertThrows(IllegalStateException.class,
				() -> odeFunctionUtil.invokeFunction(invocable, new double[] { 0 }, 1));
	}

	@ParameterizedTest
	@EnumSource
	void invokeFunctionReturnNull(final Language language) {
		final OdeFunctionUtil odeFunctionUtil = new OdeFunctionUtil(language);
		final Invocable invocable = odeFunctionUtil.prepareFunction("y[1]");

		assertThrows(IllegalStateException.class,
				() -> odeFunctionUtil.invokeFunction(invocable, new double[] { 0 }, 1));
	}

}
