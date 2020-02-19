package com.daloji.blockchain.core.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
@Documented
@Target({ FIELD, TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@ReportAsSingleViolation
public @interface BlockConstraint {
	
	  Class<?>[] groups() default {};

	  String message() default "Attribut conditionnel manquant";

	  Class<? extends Payload>[] payload() default {};
}
