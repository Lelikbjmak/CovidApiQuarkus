package com.innowise.covidapi.service;

import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.entity.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    void save(Country country);

    void saveAll(Iterable<Country> countryCollection);

    Optional<Country> findByName(String name);

    List<CountryDto> findAll();
}
