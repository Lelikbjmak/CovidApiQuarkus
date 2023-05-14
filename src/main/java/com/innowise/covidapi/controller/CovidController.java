package com.innowise.covidapi.controller;

import com.innowise.covidapi.annotation.ValidCountry;
import com.innowise.covidapi.annotation.ValidDate;
import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.dto.MaxMinCovidDetailsDto;
import com.innowise.covidapi.service.CountryService;
import com.innowise.covidapi.service.MaxMinCovidDetailsEvaluatorService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.Form;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Path("/api/v1/covid/countries")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CovidController {

    private final MaxMinCovidDetailsEvaluatorService covidDetailsEvaluatorService;

    private final CountryService countryService;

    @GET
    public Response getCountryList() {
        List<CountryDto> countryList = countryService.findAll();
        return Response.ok(countryList).build();
    }

    @POST
    @Path("/summary")
    public Response getCovidDetailsForToday(@Form @ValidCountry List<String> countryNameList) {
        MaxMinCovidDetailsDto todayCovidDetails = covidDetailsEvaluatorService.getCovidDetailsForToday(countryNameList);
        return Response.status(Response.Status.OK).entity(todayCovidDetails).build();
    }

    @POST
    @Path("/day/{day}")
    public Response getCovidDetailsForCertainDay(@Form @ValidCountry List<String> countryNameList, @PathParam("day") @ValidDate LocalDate day) {
        MaxMinCovidDetailsDto dayCovidDetails = covidDetailsEvaluatorService.getCovidDetailsForCertainDay(countryNameList, day);
        return Response.status(Response.Status.OK).entity(dayCovidDetails).build();
    }

    @POST
    @Path("/term")
    public Response getCovidDetailsForTerm(@Form @ValidCountry List<String> countryNameList, @QueryParam("from") @ValidDate LocalDate firstDate, @QueryParam("to") @ValidDate LocalDate lastDate) {
        MaxMinCovidDetailsDto termCovidDetails = covidDetailsEvaluatorService.getCovidDetailsForTerm(countryNameList, firstDate, lastDate);
        return Response.status(Response.Status.OK).entity(termCovidDetails).build();
    }

}

