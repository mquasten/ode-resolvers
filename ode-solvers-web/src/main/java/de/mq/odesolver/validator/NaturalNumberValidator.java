package de.mq.odesolver.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NaturalNumberValidator implements ConstraintValidator<NaturalNumberConstraint, String> {

	@Override
	public void initialize(final NaturalNumberConstraint constraint) {
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext cxt) {
		try {
			if (Integer.parseInt(value.strip()) <= 0) {
				return false;
			}
			return true;
		} catch (NumberFormatException numberFormatException) {
			return false;
		}
	}

}