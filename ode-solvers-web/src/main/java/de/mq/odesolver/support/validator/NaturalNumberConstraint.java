package de.mq.odesolver.support.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = NaturalNumberValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NaturalNumberConstraint {
    String message() default "keine Zahl > 0";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
