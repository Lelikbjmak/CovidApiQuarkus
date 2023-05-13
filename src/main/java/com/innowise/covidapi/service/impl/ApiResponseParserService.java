package com.innowise.covidapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.exception.ExternalApiException;
import com.innowise.covidapi.service.ApiClientService;
import com.innowise.covidapi.util.ApplicationConstant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ApiResponseParserService {

    @Inject
    @RestClient
    private ApiClientService apiClientService;

    private final ObjectMapper objectMapper;

    public List<CountryCovidDetailsDto> getTodayCovidDetailsFromApi() {
        Response response = apiClientService.getTodayCovidDetails();

        if (response.getStatus() != 200) {
            throw new ExternalApiException("External API error.", response.getStatus(), response.readEntity(String.class));
        }

        String jsonEntity = response.readEntity(String.class);

        try {
            JsonNode jsonNode;
            jsonNode = objectMapper.readTree(jsonEntity);
            ArrayNode countriesNode = (ArrayNode) jsonNode.get("Countries");
            return objectMapper.convertValue(countriesNode, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error(e.getMessage() + "Can't read JSON response from external API to obtain current summary about covid cases.");
        }

        return new ArrayList<>();
    }

    public List<CountryCovidDetailsDto> getTermCovidDetailsFromApi(List<String> countrySlugList, LocalDate firstDate, LocalDate lastDate) {
        return countrySlugList.stream().map(slug -> {

            Response response = apiClientService.getTermCovidDetails(slug, firstDate, lastDate);

            if (response.getStatus() != 200) {
                throw new ExternalApiException("External API error.", response.getStatus(), response.readEntity(String.class));
            }

            refreshApiCallRateLimit(response);

            List<CountryCovidDetailsDto> countryCovidDetailsDtoList = response.readEntity(new GenericType<>() {
            });

            List<CountryCovidDetailsDto> a = countryCovidDetailsDtoList.subList(0, countryCovidDetailsDtoList.size() - 1);
            // due to the fact that the last presented in List items describes overall stats for

            return new CountryCovidDetailsDto(a.get(0).getCountry(), a.get(a.size() - 1).getTotalCases() - a.get(0).getTotalCases(), a.get(a.size() - 1).getTotalCases(), firstDate);
        }).toList();
    }

    public List<CountryDto> getCountryListFromApi() {

        Response response = apiClientService.getPossibleCountryList();

        if (response.getStatus() != 200) {
            throw new ExternalApiException("External API error.", response.getStatus(), response.readEntity(String.class));
        }

        return response.readEntity(new GenericType<>() {
        });
    }

    private void refreshApiCallRateLimit(Response apiResponse) {

        int remainAttempts = Integer.parseInt(apiResponse.getHeaderString(ApplicationConstant.CovidApi.RATE_LIMIT_RESET_HEADER_NAME));

        if (remainAttempts == 0) {
            try {
                Thread.sleep(ApplicationConstant.CovidApi.RATE_LIMIT_RESET_HEADER_VALUE);
            } catch (InterruptedException e) {
                log.error(e.getMessage() + "Can't stop Thread to refresh rate limit to make another request to external API.");
            }
        }

    }
}
