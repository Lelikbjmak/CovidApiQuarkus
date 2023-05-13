package com.innowise.covidapi.annotation;

import com.innowise.covidapi.validator.CountryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCountry {

    String message() default "Not valid country";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
