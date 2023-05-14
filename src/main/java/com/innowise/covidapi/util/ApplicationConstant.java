package com.innowise.covidapi.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class ApplicationConstant {

    @UtilityClass
    public static class CovidApi {
        public static final String RATE_LIMIT_RESET_HEADER_NAME = "X-Ratelimit-Remaining";

        public static final long RATE_LIMIT_RESET_HEADER_VALUE = 5000;

        public static final LocalDate FIRST_RECORDER_DATE = LocalDate.of(2020, 2, 25);
    }

    @UtilityClass
    public static class Message {
        public static final String NOT_VALID_COUNTRY_MESSAGE = "Some country in List is not found.";

        public static final String NOT_VALID_DATE_MESSAGE = "Date should be after 2020-02-25 and before current date.";
    }
}
