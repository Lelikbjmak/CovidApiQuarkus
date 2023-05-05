package com.innowise.covidapi.service.impl;

import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.dto.MaxMinCovidDetailsDto;
import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import com.innowise.covidapi.mapper.CovidDetailsMapper;
import com.innowise.covidapi.service.CountryService;
import com.innowise.covidapi.service.MaxMinCovidDetailsEvaluatorService;
import com.innowise.covidapi.service.CovidDetailsService;
import com.innowise.covidapi.util.CovidDetailsIdProvider;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class MaxMinCovidDetailsEvaluatorServiceImpl implements MaxMinCovidDetailsEvaluatorService {


    private final CovidDetailsService covidDetailsService;

    private final CovidDetailsMapper covidDetailsMapper;

    private final CountryService countryService;

    private final ApiResponseParserService apiResponseParserService;

    @Override
    public MaxMinCovidDetailsDto getCovidDetailsForToday(List<String> countryNameList) {

        List<CountryCovidDetailsId> requestedCovidDetailsIdList = CovidDetailsIdProvider.getCovidDetailsIdListForCountryListForDate(countryNameList, LocalDate.now());

        List<CountryCovidDetailsDto> presentedInDatabaseCovidDetailsList = covidDetailsService.findByIdList(requestedCovidDetailsIdList);

        if (presentedInDatabaseCovidDetailsList.size() == countryNameList.size())
            return evaluateMaxAndMinCovidDetailsForCovidDetailsList(presentedInDatabaseCovidDetailsList);

        List<String> remainCountryNameList = getRemainCountryNameList(countryNameList, presentedInDatabaseCovidDetailsList);

        // All covid info for today from API
        List<CountryCovidDetailsDto> todayApiCovidDetailsDtoList = apiResponseParserService.getTodayCovidDetailsFromApi();
        List<CountryCovidDetailsDto> notPresentedInDatabaseCovidDetails = covidDetailsService.ascertainCovidDetailsNotPresentedInDatabase(remainCountryNameList, todayApiCovidDetailsDtoList);

        notPresentedInDatabaseCovidDetails.forEach(countryCovidDetailsDto ->
                covidDetailsService.save(covidDetailsMapper.mapToEntity(countryCovidDetailsDto)));

        List<CountryCovidDetailsDto> completeCovidDetailsList = new ArrayList<>();
        completeCovidDetailsList.addAll(notPresentedInDatabaseCovidDetails);
        completeCovidDetailsList.addAll(presentedInDatabaseCovidDetailsList);

        return evaluateMaxAndMinCovidDetailsForCovidDetailsList(completeCovidDetailsList);
    }

    @Override
    public MaxMinCovidDetailsDto getCovidDetailsForCertainDay(List<String> countryNameList, LocalDate date) {

        // id of countries we wanna obtain info for
        List<CountryCovidDetailsId> requestedCovidDetailsIdList = CovidDetailsIdProvider.getCovidDetailsIdListForCountryListForDate(countryNameList, date);

        List<CountryCovidDetailsDto> presentedInDatabaseCovidDetailsList = covidDetailsService.findByIdList(requestedCovidDetailsIdList);

        if (presentedInDatabaseCovidDetailsList.size() == countryNameList.size())
            return evaluateMaxAndMinCovidDetailsForCovidDetailsList(presentedInDatabaseCovidDetailsList);

        List<String> remainCountryNameList = getRemainCountryNameList(countryNameList, presentedInDatabaseCovidDetailsList);

        List<String> remainCountrySlugList = countryService.getSlugListByCountryNameList(countryNameList);

        List<CountryCovidDetailsDto> getDyaApiCovidDetailsDtoList = apiResponseParserService.getTermCovidDetailsFromApi(remainCountrySlugList, date, date.plusDays(2)); // plus 2 due to API incorrect work
        List<CountryCovidDetailsDto> notPresentedInDatabaseCovidDetails = covidDetailsService.ascertainCovidDetailsNotPresentedInDatabase(remainCountryNameList, getDyaApiCovidDetailsDtoList);

        notPresentedInDatabaseCovidDetails.forEach(countryCovidDetailsDto ->
                covidDetailsService.save(covidDetailsMapper.mapToEntity(countryCovidDetailsDto)));

        List<CountryCovidDetailsDto> completeCovidDetailsList = new ArrayList<>();
        completeCovidDetailsList.addAll(notPresentedInDatabaseCovidDetails);
        completeCovidDetailsList.addAll(presentedInDatabaseCovidDetailsList);

        return evaluateMaxAndMinCovidDetailsForCovidDetailsList(completeCovidDetailsList);
    }

    @Override
    public MaxMinCovidDetailsDto getCovidDetailsForTerm(List<String> countryNameList, LocalDate firstDate, LocalDate lastDate) {

        List<CountryCovidDetailsDto> presentedInDatabaseCovidDetailsList = covidDetailsService.getCovidDetailsListForCountryListForTermFromDatabase(countryNameList, firstDate, lastDate);

        if (presentedInDatabaseCovidDetailsList.size() == countryNameList.size())
            return evaluateMaxAndMinCovidDetailsForCovidDetailsList(presentedInDatabaseCovidDetailsList);

        List<String> remainCountryNameList = getRemainCountryNameList(countryNameList, presentedInDatabaseCovidDetailsList);

        List<String> remainCountrySlugList = countryService.getSlugListByCountryNameList(countryNameList);

        List<CountryCovidDetailsDto> getDyaApiCovidDetailsDtoList = apiResponseParserService.getTermCovidDetailsFromApi(remainCountrySlugList, firstDate, lastDate.plusDays(2)); // plus 2 due to API incorrect work
        List<CountryCovidDetailsDto> notPresentedInDatabaseCovidDetails = covidDetailsService.ascertainCovidDetailsNotPresentedInDatabase(remainCountryNameList, getDyaApiCovidDetailsDtoList);

        notPresentedInDatabaseCovidDetails.forEach(countryCovidDetailsDto ->
                covidDetailsService.save(covidDetailsMapper.mapToEntity(countryCovidDetailsDto)));

        List<CountryCovidDetailsDto> completeCovidDetailsList = new ArrayList<>();
        completeCovidDetailsList.addAll(notPresentedInDatabaseCovidDetails);
        completeCovidDetailsList.addAll(presentedInDatabaseCovidDetailsList);

        return evaluateMaxAndMinCovidDetailsForCovidDetailsList(completeCovidDetailsList);
    }

    private List<String> getRemainCountryNameList(List<String> countryNameList, List<CountryCovidDetailsDto> covidDetailsDtoListFromDatabase) {

        List<String> foundCountryName = covidDetailsDtoListFromDatabase.stream()
                .map(CountryCovidDetailsDto::getCountry)
                .toList();

        countryNameList.removeAll(foundCountryName);

        return countryNameList;
    }

    private MaxMinCovidDetailsDto evaluateMaxAndMinCovidDetailsForCovidDetailsList(List<CountryCovidDetailsDto> covidDetailsDtoList) {

        long maxCasesValue = covidDetailsDtoList.stream()
                .mapToLong(CountryCovidDetailsDto::getCases)
                .max().orElse(0L);

        long minCasesValue = covidDetailsDtoList.stream()
                .mapToLong(CountryCovidDetailsDto::getCases)
                .min().orElse(0L);

        List<CountryCovidDetailsDto> minCasesCountryDetails = covidDetailsDtoList.stream()
                .filter(countryCovidDetailsDto -> countryCovidDetailsDto.getCases() == minCasesValue)
                .toList();

        List<CountryCovidDetailsDto> maxCasesCountryDetails = covidDetailsDtoList.stream()
                .filter(countryCovidDetailsDto -> countryCovidDetailsDto.getCases() == maxCasesValue)
                .toList();

        return new MaxMinCovidDetailsDto(minCasesCountryDetails, maxCasesCountryDetails);
    }

}
