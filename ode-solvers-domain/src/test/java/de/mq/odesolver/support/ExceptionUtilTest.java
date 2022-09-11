package de.mq.odesolver.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ExceptionUtilTest {

	@Test
	void translateCheckedException() {
		final var exception = new Exception();
		final var result = ExceptionUtil.translateToRuntimeException(exception);
		assertTrue(result instanceof IllegalStateException);
		assertEquals(exception, result.getCause());

	}
	@Test
	void translate() {
		final var exception = new IllegalArgumentException();
		assertEquals(exception, ExceptionUtil.translateToRuntimeException(exception));
	}

}
