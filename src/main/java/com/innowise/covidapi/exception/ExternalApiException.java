package com.innowise.covidapi.exception;

import lombok.Getter;

public class ExternalApiException extends RuntimeException {

    @Getter
    private int statusCode;

    @Getter
    private String jsonData;

    public ExternalApiException(int statusCode, String jsonData) {
        this.statusCode = statusCode;
        this.jsonData = jsonData;
    }

    public ExternalApiException(String message, int statusCode, String jsonData) {
        super(message);
        this.statusCode = statusCode;
        this.jsonData = jsonData;
    }

    public ExternalApiException(String message, Throwable cause, int statusCode, String jsonData) {
        super(message, cause);
        this.statusCode = statusCode;
        this.jsonData = jsonData;
    }

    public ExternalApiException(Throwable cause, int statusCode, String jsonData) {
        super(cause);
        this.statusCode = statusCode;
        this.jsonData = jsonData;
    }
}
