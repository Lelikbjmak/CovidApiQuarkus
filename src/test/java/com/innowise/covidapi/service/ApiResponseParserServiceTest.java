package com.innowise.covidapi.service;

import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.service.impl.ApiResponseParserService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;


@QuarkusTest
class ApiResponseParserServiceTest {

    @InjectMock
    @RestClient
    private ApiClientService apiClientService;

    @Inject
    private ApiResponseParserService apiResponseParserService;

    @Test
    void getTodayCovidDetailsFromApi() {
        final String todayCovidDetailsJson =
                "{\"Countries\": [{\"Country\":\"Japan\",\"CountryCode\":\"JP\",\"Slug\":\"japan\",\"NewConfirmed\":522,\"TotalConfirmed\":3139,\"NewDeaths\":14,\"TotalDeaths\":77,\"NewRecovered\":0,\"TotalRecovered\":514,\"Date\":\"2020-04-05T06:37:00Z\"}]}";
        Mockito.when(apiClientService.getTodayCovidDetails()).thenReturn(Response.ok().entity(todayCovidDetailsJson).build());

        List<CountryCovidDetailsDto> covidDetailsDtoList = apiResponseParserService.getTodayCovidDetailsFromApi();
        final CountryCovidDetailsDto expectedDto = new CountryCovidDetailsDto("Japan", 522, 3139, LocalDate.of(2020, 4, 5));

        Assertions.assertNotNull(covidDetailsDtoList);
        Assertions.assertTrue(covidDetailsDtoList.contains(expectedDto));
    }

    @Test
    void getTermCovidDetailsFromApi() {
        CountryCovidDetailsDto firstDateDetails = new CountryCovidDetailsDto("Japan", 522, 3139, LocalDate.of(2020, 3, 14));
        CountryCovidDetailsDto middleDateDetails = new CountryCovidDetailsDto("Japan", 222, 3339, LocalDate.of(2020, 3, 15));
        CountryCovidDetailsDto lastDateDetails = new CountryCovidDetailsDto("Japan", 222, 3939, LocalDate.of(2020, 3, 16));

        List<CountryCovidDetailsDto> responseJson = List.of(
                firstDateDetails,
                middleDateDetails,
                lastDateDetails
        );

        final LocalDate firstDate = LocalDate.of(2021, 3, 14);
        final LocalDate lastDate = LocalDate.of(2021, 3, 16);
        final String countrySlug = "japan";

        CountryCovidDetailsDto expected = new CountryCovidDetailsDto("Japan", 200, 3339, firstDate);

        final Response response = Response.ok().entity(responseJson).header("X-Ratelimit-Remaining", 3).type(MediaType.APPLICATION_JSON_TYPE).build();

        Mockito.when(apiClientService.getTermCovidDetails(countrySlug, firstDate, lastDate)).thenReturn(response);

        List<CountryCovidDetailsDto> covidDetailsDtoList = apiResponseParserService.getTermCovidDetailsFromApi(List.of(countrySlug), firstDate, lastDate);
        Assertions.assertNotNull(covidDetailsDtoList);
        covidDetailsDtoList.forEach(System.out::println);
        Assertions.assertEquals(1, covidDetailsDtoList.size());
        Assertions.assertEquals(expected, covidDetailsDtoList.get(0));
    }

    @Test
    void getCountryListFromApi() {
        List<CountryDto> countryListJson = List.of(
                new CountryDto("Japan", "japan", "JP")
        );

        Mockito.when(apiClientService.getPossibleCountryList()).thenReturn(Response.ok().entity(countryListJson).build());

        List<CountryDto> countryDtoList = apiResponseParserService.getCountryListFromApi();
        Assertions.assertNotNull(countryDtoList);
        Assertions.assertEquals(1, countryDtoList.size());
    }
}