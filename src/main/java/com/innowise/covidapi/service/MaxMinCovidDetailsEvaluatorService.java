package com.innowise.covidapi.service;

import com.innowise.covidapi.dto.MaxMinCovidDetailsDto;

import java.time.LocalDate;
import java.util.List;

public interface MaxMinCovidDetailsEvaluatorService {

    MaxMinCovidDetailsDto getCovidDetailsForToday(List<String> countryNameList);

    MaxMinCovidDetailsDto getCovidDetailsForCertainDay(List<String> countryNameList, LocalDate date);

    MaxMinCovidDetailsDto getCovidDetailsForTerm(List<String> countryNameList, LocalDate firstDate, LocalDate lastDate);

}
