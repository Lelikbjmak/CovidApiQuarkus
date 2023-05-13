package com.innowise.covidapi.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.covidapi.exception.ExternalApiException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Provider
@RequiredArgsConstructor
public class ExternalApiExceptionHandler implements ExceptionMapper<ExternalApiException> {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Response toResponse(ExternalApiException e) {

        log.error(e.getMessage());
        Map<String, Object> data = objectMapper.readValue(e.getJsonData(), Map.class);
        return Response.status(e.getStatusCode())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(data)
                .build();
    }
}
