package de.mq.odesolver.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DoubleValidatorTest {

	private final DoubleValidator doubleValidator = new DoubleValidator();

	@ParameterizedTest
	@ValueSource(strings = { "", " ", "47.11", " 1 " })
	void isValid(final String realNumber) {
		assertTrue(doubleValidator.isValid(realNumber, null));
	}

	@ParameterizedTest
	@ValueSource(strings = { "x", "47,11", "Infinity", "NaN" })
	void isValidFalse(final String realNumber) {
		assertFalse(doubleValidator.isValid(realNumber, null));
	}

	@Test
	void initialize() {
		doubleValidator.initialize(null);
	}

}
