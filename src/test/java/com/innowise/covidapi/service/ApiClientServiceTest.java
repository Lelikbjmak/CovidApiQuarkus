package com.innowise.covidapi.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@QuarkusTest
class ApiClientServiceTest {

    @Inject
    @RestClient
    private ApiClientService apiClientService;

    @Test
    void getPossibleCountryList() throws InterruptedException {
        Response response = apiClientService.getPossibleCountryList();
        String json = response.readEntity(String.class);

        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertNotNull(json);

        Assertions.assertTrue(json.contains("Country"));
        Assertions.assertTrue(json.contains("Slug"));
        Assertions.assertTrue(json.contains("ISO2"));

        Thread.sleep(3000); // To avoid 429 status from API
    }

    @Test
    void getTermCovidDetails() throws InterruptedException {
        final LocalDate firstDate = LocalDate.of(2021, 3, 14);
        final LocalDate lastDate = LocalDate.of(2021, 3, 18);
        final String countrySlug = "japan";
        final String country = "Japan";

        Response response = apiClientService.getTermCovidDetails(countrySlug, firstDate, lastDate);
        String json = response.readEntity(String.class);

        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertNotNull(json);

        Assertions.assertTrue(json.contains("Country"));
        Assertions.assertTrue(json.contains("Cases"));
        Assertions.assertTrue(json.contains("Date"));
        Assertions.assertTrue(json.contains(country));

        Thread.sleep(3000); // To avoid 429 status from API
    }

    @Test
    void getTodayCovidDetails() {
        Response response = apiClientService.getTodayCovidDetails();
        String json = response.readEntity(String.class);

        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertNotNull(json);

        Assertions.assertTrue(json.contains("Global"));
        Assertions.assertTrue(json.contains("Countries"));
    }
}