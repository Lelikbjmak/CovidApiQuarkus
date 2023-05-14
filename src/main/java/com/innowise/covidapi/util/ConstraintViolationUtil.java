package com.innowise.covidapi.util;

import jakarta.validation.ConstraintViolationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstraintViolationUtil {

    public static String getMessage(ConstraintViolationException exception) {

        StringBuilder message = new StringBuilder();

        exception.getConstraintViolations()
                .forEach(error -> message.append(error.getMessage()).append(" "));

        return message.toString().trim();
    }
}
