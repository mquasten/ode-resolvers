package de.mq.odesolver.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

public class DoubleArrayValidator implements ConstraintValidator<DoubleArrayConstraint, String> {

	public static final String REGEX_SPLIT_DOUBLE_VECTOR = "[,; ]";
	private final DoubleValidator doubleValidator = new DoubleValidator();

	@Override
	public void initialize(final DoubleArrayConstraint constraint) {
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext cxt) {

		if (!StringUtils.hasText(value)) {
			return false;
		}

		final String values[] = value.split(REGEX_SPLIT_DOUBLE_VECTOR);
		for (int i = 0; i < values.length; i++) {
			if (!StringUtils.hasText(values[i])) {
				return false;
			}

			if (!doubleValidator.isValid(StringUtils.trimWhitespace(values[i]), cxt)) {
				return false;
			}
		}
		return true;
	}

}