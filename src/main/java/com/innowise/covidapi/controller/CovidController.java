package com.innowise.covidapi.controller;

import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.dto.MaxMinCovidDetailsDto;
import com.innowise.covidapi.service.MaxMinCovidDetailsEvaluatorService;
import com.innowise.covidapi.service.impl.ApiResponseParserService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.Form;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Path("/api/v1/covid/countries")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class CovidController {

    private final ApiResponseParserService apiResponseParserService;

    private final MaxMinCovidDetailsEvaluatorService covidDetailsEvaluatorService;

    @GET
    public Response getCountryList() {
        List<CountryDto> a = apiResponseParserService.getCountryListFromApi();
        return Response.ok(a).build();
    }

    @POST
    @Path("/summary")
    public Response getCovidDetails(@Form List<String> countryNameList) {
        MaxMinCovidDetailsDto todayCovidDetails = covidDetailsEvaluatorService.getCovidDetailsForToday(countryNameList);
        return Response.status(Response.Status.OK).entity(todayCovidDetails).build();
    }

    @POST
    @Path("/day/{day}")
    public Response getCovidDetails(@Form List<String> countryNameList, @PathParam("day") LocalDate day) {
        MaxMinCovidDetailsDto dayCovidDetails = covidDetailsEvaluatorService.getCovidDetailsForCertainDay(countryNameList, day);
        return Response.status(Response.Status.OK).entity(dayCovidDetails).build();
    }


    @POST
    @Path("/term")
    public Response getCovidDetailsForTerm(@Form List<String> countryNameList, @QueryParam("from") LocalDate firstDate, @QueryParam("to") LocalDate lastDate) {
        MaxMinCovidDetailsDto termCovidDetails = covidDetailsEvaluatorService.getCovidDetailsForTerm(countryNameList, firstDate, lastDate);
        return Response.status(Response.Status.OK).entity(termCovidDetails).build();
    }

}

