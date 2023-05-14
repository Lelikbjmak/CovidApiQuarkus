package com.innowise.covidapi.controller;

import com.innowise.covidapi.config.TestResourceConfig;
import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.dto.MaxMinCovidDetailsDto;
import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import com.innowise.covidapi.service.CountryService;
import com.innowise.covidapi.service.CovidDetailsService;
import com.innowise.covidapi.service.impl.ApiResponseParserService;
import com.innowise.covidapi.service.impl.CountryCovidDetailsRedisService;
import com.innowise.covidapi.util.ApplicationConstant;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.OK;

@QuarkusTest
@TestProfile(TestResourceConfig.class)
class CovidControllerTest {

    @Inject
    private CountryService countryService;

    @Inject
    private ApiResponseParserService apiResponseParserService;

    @Inject
    private CovidDetailsService covidDetailsService;

    @Inject
    private CountryCovidDetailsRedisService countryCovidDetailsRedisService;

    @Test
    void getCountryList() {

        List<CountryDto> expectedCountryList = countryService.findAll();

        Response response = given()
                .when().get("/api/v1/covid/countries")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<CountryDto> actualCountryList = response.body().as(new TypeRef<>() {
        });

        Assertions.assertEquals(expectedCountryList, actualCountryList);
    }

    @Test
    void getCovidDetailsForToday() {
        List<CountryCovidDetailsDto> covidDetailsFromApi = apiResponseParserService.getTodayCovidDetailsFromApi();

        MaxMinCovidDetailsDto summaryCovidDetails = given()
                .contentType(ContentType.JSON)
                .body(List.of("Belarus", "Japan"))
                .when().post("/api/v1/covid/countries/summary")
                .then()
                .statusCode(OK.getStatusCode())
                .extract().as(MaxMinCovidDetailsDto.class);

        Assertions.assertNotNull(summaryCovidDetails);
        Assertions.assertTrue(covidDetailsFromApi.containsAll(summaryCovidDetails.getMin()));
        Assertions.assertTrue(countryCovidDetailsRedisService
                .findById(new CountryCovidDetailsId("Belarus", LocalDate.now())).isPresent());
        Assertions.assertTrue(countryCovidDetailsRedisService
                .findById(new CountryCovidDetailsId("Japan", LocalDate.now())).isPresent());
        Assertions.assertTrue(covidDetailsFromApi.containsAll(summaryCovidDetails.getMax()));
    }

    @Test
    void getCovidDetailsForTodayCountryNotFound() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(List.of("Belaru", "Japan"))
                .when().post("/api/v1/covid/countries/summary")
                .then()
                .statusCode(NOT_FOUND.getStatusCode())
                .extract()
                .response();

        Map<String, Object> responseEntity = response.body().as(new TypeRef<>() {
        });

        Assertions.assertTrue(responseEntity.containsKey("message"));
        Assertions.assertEquals(ApplicationConstant.Message.NOT_VALID_COUNTRY_MESSAGE, responseEntity.get("message"));
    }

    @Test
    void getCovidDetailsForCertainDay() {
        MaxMinCovidDetailsDto certainDayCovidDetails = given()
                .contentType(ContentType.JSON)
                .body(List.of("Belarus", "Japan"))
                .pathParam("day", "2021-02-14")
                .when().post("/api/v1/covid/countries/day/{day}")
                .then()
                .statusCode(OK.getStatusCode())
                .extract().as(MaxMinCovidDetailsDto.class);

        Assertions.assertNotNull(certainDayCovidDetails);
        Assertions.assertNotNull(certainDayCovidDetails.getMin());
        Assertions.assertNotNull(certainDayCovidDetails.getMin());
        Assertions.assertTrue(covidDetailsService
                .findById(new CountryCovidDetailsId("Belarus", LocalDate.of(2021, 2, 14))).isPresent());
        Assertions.assertTrue(covidDetailsService
                .findById(new CountryCovidDetailsId("Japan", LocalDate.of(2021, 2, 14))).isPresent());
        Assertions.assertEquals(1, certainDayCovidDetails.getMin().size());
        Assertions.assertEquals(1, certainDayCovidDetails.getMax().size());
    }

    @Test
    void getCovidDetailsForCertainDayCountryNotFound() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(List.of("Belaru", "Japan"))
                .pathParam("day", "2021-03-14")
                .when().post("/api/v1/covid/countries/day/{day}")
                .then()
                .statusCode(NOT_FOUND.getStatusCode())
                .extract().response();
        Map<String, Object> responseEntity = response.body().as(new TypeRef<>() {
        });

        Assertions.assertTrue(responseEntity.containsKey("message"));
        Assertions.assertEquals(ApplicationConstant.Message.NOT_VALID_COUNTRY_MESSAGE, responseEntity.get("message"));
    }

    @Test
    void getCovidDetailsForCertainDayNotValidDate() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(List.of("Belarus", "Japan"))
                .pathParam("day", "2011-03-14")
                .when().post("/api/v1/covid/countries/day/{day}")
                .then()
                .statusCode(NOT_FOUND.getStatusCode())
                .extract().response();
        Map<String, Object> responseEntity = response.body().as(new TypeRef<>() {
        });

        Assertions.assertTrue(responseEntity.containsKey("message"));
        Assertions.assertEquals(ApplicationConstant.Message.NOT_VALID_DATE_MESSAGE, responseEntity.get("message"));
    }

    @Test
    void getCovidDetailsForTerm() {
        MaxMinCovidDetailsDto certainDayCovidDetails = given()
                .contentType(ContentType.JSON)
                .body(List.of("Belarus", "Japan"))
                .queryParam("from", "2021-03-14")
                .queryParam("to", "2021-03-16")
                .when().post("/api/v1/covid/countries/term")
                .then()
                .statusCode(OK.getStatusCode())
                .extract().as(MaxMinCovidDetailsDto.class);

        Assertions.assertNotNull(certainDayCovidDetails);
        Assertions.assertNotNull(certainDayCovidDetails.getMin());
        Assertions.assertNotNull(certainDayCovidDetails.getMin());
        Assertions.assertEquals(1, certainDayCovidDetails.getMin().size());
        Assertions.assertEquals(1, certainDayCovidDetails.getMax().size());
    }

    @Test
    void getCovidDetailsForTermCountryNotFound() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(List.of("Belaru", "Japan"))
                .queryParam("from", "2021-03-14")
                .queryParam("to", "2021-03-16")
                .when().post("/api/v1/covid/countries/term")
                .then()
                .statusCode(NOT_FOUND.getStatusCode())
                .extract().response();
        Map<String, Object> responseEntity = response.body().as(new TypeRef<>() {
        });

        Assertions.assertTrue(responseEntity.containsKey("message"));
        Assertions.assertEquals(ApplicationConstant.Message.NOT_VALID_COUNTRY_MESSAGE, responseEntity.get("message"));
    }

    @Test
    void getCovidDetailsForTermInvalidDate() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(List.of("Belarus", "Japan"))
                .queryParam("from", "2020-03-14")
                .queryParam("to", "2045-03-16")
                .when().post("/api/v1/covid/countries/term")
                .then()
                .statusCode(NOT_FOUND.getStatusCode())
                .extract().response();
        Map<String, Object> responseEntity = response.body().as(new TypeRef<>() {
        });

        Assertions.assertTrue(responseEntity.containsKey("message"));
        Assertions.assertEquals(ApplicationConstant.Message.NOT_VALID_DATE_MESSAGE, responseEntity.get("message"));
    }

}