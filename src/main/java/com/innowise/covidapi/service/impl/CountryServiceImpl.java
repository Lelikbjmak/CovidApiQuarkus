package com.innowise.covidapi.service.impl;

import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.entity.Country;
import com.innowise.covidapi.mapper.CountryListMapper;
import com.innowise.covidapi.repository.CountryRepository;
import com.innowise.covidapi.service.CountryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryListMapper countryListMapper;

    @Override
    @Transactional
    public void save(Country country) {
        countryRepository.persist(country);
    }

    @Override
    @Transactional
    public void saveAll(Iterable<Country> countryCollection) {
        countryRepository.persist(countryCollection);
    }

    @Override
    @Transactional
    public Optional<Country> findByName(String name) {
        return countryRepository.findByName(name);
    }

    @Override
    public List<CountryDto> findAll() {
        List<Country> countryListFromDatabase = countryRepository.findAll().list();
        return countryListMapper.mapToDtoList(countryListFromDatabase);
    }
}
