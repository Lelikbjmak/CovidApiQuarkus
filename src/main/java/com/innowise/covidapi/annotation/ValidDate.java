package com.innowise.covidapi.annotation;

import com.innowise.covidapi.validator.DateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target(value = {ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ValidDate {

    String message() default "Not valid date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
