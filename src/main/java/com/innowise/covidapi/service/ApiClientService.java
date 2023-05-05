package com.innowise.covidapi.service;

import com.innowise.covidapi.dto.CountryDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.LocalDate;
import java.util.List;

@RegisterRestClient(configKey = "covid-api")
public interface ApiClientService {

    @GET
    @Path("/countries")
    List<CountryDto> getPossibleCountryList();

    @GET
    @Path("/total/country/{country}/status/confirmed")
    Response getTermCovidDetails(@PathParam(value = "country") String countrySlug,
                                 @QueryParam("from") LocalDate firstDate, @QueryParam("to") LocalDate lastDay);

    @GET
    @Path("summary")
    String getTodayCovidDetailsJson();

}
