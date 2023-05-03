package com.innowise.covidapi.service;

import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.entity.CountryCovidDetails;
import com.innowise.covidapi.entity.id.CountryCovidDetailsId;

import java.util.List;
import java.util.Optional;

public interface CovidDetailsService {

    void save(CountryCovidDetails countryCovidDetails);

    Optional<CountryCovidDetails> findById(CountryCovidDetailsId id);

    List<CountryCovidDetailsDto> findByIdList(List<CountryCovidDetailsId> idList);
}
