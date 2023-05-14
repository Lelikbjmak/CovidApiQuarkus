package com.innowise.covidapi.config;

import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.entity.Country;
import com.innowise.covidapi.mapper.CountryListMapper;
import com.innowise.covidapi.service.CountryService;
import com.innowise.covidapi.service.impl.ApiResponseParserService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Startup
@Singleton
@RequiredArgsConstructor
public class StartupConfig {

    private final ApiResponseParserService apiResponseParserService;

    private final CountryListMapper countryListMapper;

    private final CountryService countryService;

    @PostConstruct
    public void onStartup() {
        List<CountryDto> countryDtoListFromApi = apiResponseParserService.getCountryListFromApi();
        List<Country> countryList = countryListMapper.mapToEntityList(countryDtoListFromApi);
        List<Country> presentedCountryListInDatabase = countryListMapper.mapToEntityList(countryService.findAll());
        countryList.removeAll(presentedCountryListInDatabase); // obtain only countries we don't have in Database
        countryService.saveAll(countryList);
    }
}
