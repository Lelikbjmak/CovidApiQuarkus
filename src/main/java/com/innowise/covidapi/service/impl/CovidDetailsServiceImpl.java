package com.innowise.covidapi.service.impl;

import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.entity.CountryCovidDetails;
import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import com.innowise.covidapi.mapper.CovidDetailsListMapper;
import com.innowise.covidapi.repository.CovidDetailsRepository;
import com.innowise.covidapi.service.CovidDetailsService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class CovidDetailsServiceImpl implements CovidDetailsService {

    private final CovidDetailsRepository covidDetailsRepository;

    private final CovidDetailsListMapper covidDetailsListMapper;

    @Override
    @Transactional
    public void save(CountryCovidDetails countryCovidDetails) {
        covidDetailsRepository.persist(countryCovidDetails);
    }

    @Override
    public Optional<CountryCovidDetails> findById(CountryCovidDetailsId id) {
        return covidDetailsRepository.findById(id);
    }

    @Override
    public List<CountryCovidDetailsDto> findByIdList(List<CountryCovidDetailsId> idList) {
        List<CountryCovidDetails> countryCovidDetailsList = idList.stream()
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return covidDetailsListMapper.mapToDtoList(countryCovidDetailsList);
    }
}
