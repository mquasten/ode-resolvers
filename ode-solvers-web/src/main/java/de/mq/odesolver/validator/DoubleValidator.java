package de.mq.odesolver.validator;



import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DoubleValidator implements ConstraintValidator<DoubleConstraint, String> {

  @Override
  public void initialize(final DoubleConstraint constraint) {
  }

  @Override
  public boolean isValid(final String value,final ConstraintValidatorContext cxt) {
      
	  try {
		  if( ! StringUtils.hasText(value)) {
			  return true;
		  }
		  
		  final Double result = Double.parseDouble(value);
		  if (Double.isNaN(result)) {
				return false;
			}
			if (Double.isInfinite(result)) {
				return false;
			}
		  return true;
	  } catch (NumberFormatException numberFormatException) {
		  return false;
	  }
  }

}