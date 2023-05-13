package com.innowise.covidapi.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConstant {

    @UtilityClass
    public static class CovidApi {
        public static final String RATE_LIMIT_RESET_HEADER_NAME = "X-Ratelimit-Remaining";

        public static final long RATE_LIMIT_RESET_HEADER_VALUE = 5000;
    }

    @UtilityClass
    public static class Message {
        public static final String NOT_VALID_COUNTRY_MESSAGE = "Some country in List is not found.";
    }
}
