package de.mq.odesolver.support.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidator;


import org.junit.jupiter.api.Test;

class DoubleArrayValidatorTest {
	
	ConstraintValidator<DoubleArrayConstraint, String>  validator = new DoubleArrayValidator();
	
	@Test
	final void empty() {
		assertFalse(validator.isValid("", null));
	}
	
	@Test
	final void validate() {
		assertTrue(validator.isValid("1,2;3,4 5 6 7,8,9", null));
	}
	
	@Test
	final void validateFalse() {
		assertFalse(validator.isValid("1,x", null));
	}
	
	@Test
	final void validateEmptyMember() {
		assertFalse(validator.isValid("1, ,1", null));
	}
	
	@Test
	final void init() {
		validator.initialize(null);
	}
	
	

}
