package de.mq.odesolver.support.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

public class NaturalNumberValidator implements ConstraintValidator<NaturalNumberConstraint, String> {

	@Override
	public void initialize(final NaturalNumberConstraint constraint) {
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext cxt) {
		try {
			if (Integer.parseInt(StringUtils.trimWhitespace(value)) <= 0) {
				return false;
			}
			return true;
		} catch (NumberFormatException numberFormatException) {
			return false;
		}
	}

}