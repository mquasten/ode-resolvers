package de.mq.odesolver.validator;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NaturalNumberValidatorTest {
	
	private final  NaturalNumberValidator naturalNumberValidator = new NaturalNumberValidator();
	
	@ParameterizedTest
	@ValueSource(strings =  { " 1 " , "1" , "4711" , "" + Integer.MAX_VALUE} )
	void isValid(final String naturalNumber) {
		assertTrue(naturalNumberValidator.isValid(naturalNumber, null));
	}
	
	@ParameterizedTest
	@ValueSource(strings =  { "0" , "-1" , "x" , "" + Integer.MAX_VALUE+1} )
	void isValidFalse(final String naturalNumber) {
		assertFalse(naturalNumberValidator.isValid(naturalNumber, null));
	}
	
	@Test
	void initialize() {
		naturalNumberValidator.initialize(null);
	}

}
