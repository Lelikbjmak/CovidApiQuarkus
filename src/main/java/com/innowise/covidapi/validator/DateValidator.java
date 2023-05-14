package com.innowise.covidapi.validator;

import com.innowise.covidapi.annotation.ValidDate;
import com.innowise.covidapi.util.ApplicationConstant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

@ApplicationScoped
public class DateValidator implements ConstraintValidator<ValidDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {

        if (date.isAfter(ApplicationConstant.CovidApi.FIRST_RECORDER_DATE) && date.isBefore(LocalDate.now())) {
            return true;
        } else {
            buildConstraintViolation(constraintValidatorContext);
            return false;
        }
    }

    private void buildConstraintViolation(ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(ApplicationConstant.Message.NOT_VALID_DATE_MESSAGE)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }
}
