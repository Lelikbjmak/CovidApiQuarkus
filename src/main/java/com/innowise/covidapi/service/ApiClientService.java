package com.innowise.covidapi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.LocalDate;

@ApplicationScoped
@RegisterRestClient(configKey = "covid-api")
public interface ApiClientService {

    @GET
    @Path("/countries")
    Response getPossibleCountryList();

    @GET
    @Path("/total/country/{country}/status/confirmed")
    Response getTermCovidDetails(@PathParam(value = "country") String countrySlug,
                                 @QueryParam("from") LocalDate firstDate, @QueryParam("to") LocalDate lastDay);

    @GET
    @Path("summary")
    Response getTodayCovidDetails();

}
