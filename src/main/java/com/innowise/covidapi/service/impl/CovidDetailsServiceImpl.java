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

import java.time.LocalDate;
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

    @Override
    public List<CountryCovidDetailsDto> ascertainCovidDetailsNotPresentedInDatabase(List<String> notPresentedCountryList,
                                                                                    List<CountryCovidDetailsDto> countryCovidDetailsFromApi) {
        return countryCovidDetailsFromApi.stream()
                .filter(covidDetails -> notPresentedCountryList.contains(covidDetails.getCountry()))
                .toList();
    }

    private Optional<CountryCovidDetailsDto> getDetailsForTerm(CountryCovidDetailsId firstDateId, CountryCovidDetailsId lastDateId) {

        Optional<CountryCovidDetails> optionalDetailsForFirstDate = findById(firstDateId);
        Optional<CountryCovidDetails> optionalDetailsForLastDate = findById(lastDateId);

        CountryCovidDetailsDto termCountryCovidDetailsDto = null;

        if (optionalDetailsForFirstDate.isPresent() && optionalDetailsForLastDate.isPresent()) {

            CountryCovidDetails firstDateCovidDetails = optionalDetailsForFirstDate.get();
            CountryCovidDetails lastDateCovidDetails = optionalDetailsForLastDate.get();

            long termCases = lastDateCovidDetails.getTotalCases() - firstDateCovidDetails.getTotalCases();
            termCountryCovidDetailsDto = new CountryCovidDetailsDto(firstDateCovidDetails.getId().getCountry(), termCases, lastDateCovidDetails.getTotalCases(), firstDateCovidDetails.getId().getDate());
        }

        return Optional.ofNullable(termCountryCovidDetailsDto);
    }

    public List<CountryCovidDetailsDto> getCovidDetailsListForCountryListForTermFromDatabase(List<String> countryNameList, LocalDate firstDate, LocalDate lastDate) {
        return countryNameList.stream().map(countryName -> this.getDetailsForTerm(
                        new CountryCovidDetailsId(countryName, firstDate), new CountryCovidDetailsId(countryName, lastDate)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}

