package de.mq.odesolver.validator;



import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DoubleArrayValidator implements ConstraintValidator<DoubleArrayConstraint, String> {

	public static final String REGEX_SPLIT_DOUBLE_VECTOR = "[,; ]";
	private final DoubleValidator doubleValidator = new DoubleValidator();

	@Override
	public void initialize(final DoubleArrayConstraint constraint) {
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext cxt) {

		if (!StringUtils.hasText(value)) {
			return true;
		}

		final String values[] = value.split(REGEX_SPLIT_DOUBLE_VECTOR);
		for (int i = 0; i < values.length; i++) {
			if (!StringUtils.hasText(values[i])) {
				return false;
			}

			if (!doubleValidator.isValid(values[i].strip(), cxt)) {
				return false;
			}
		}
		return true;
	}

}