package com.innowise.covidapi.service;

import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.entity.CountryCovidDetails;
import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import com.innowise.covidapi.mapper.CovidDetailsListMapper;
import com.innowise.covidapi.repository.CovidDetailsRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class CovidDetailsServiceImplTest {

    @InjectMock
    private CovidDetailsRepository covidDetailsRepository;

    @InjectMock
    private CovidDetailsListMapper covidDetailsListMapper;

    @Inject
    private CovidDetailsService covidDetailsService;

    @Test
    void save() {
        CountryCovidDetails countryCovidDetails = new CountryCovidDetails();
        covidDetailsService.save(countryCovidDetails);
        Mockito.verify(covidDetailsRepository, Mockito.times(1)).persist(countryCovidDetails);
    }

    @Test
    void findById() {
        final CountryCovidDetailsId covidDetailsId = new CountryCovidDetailsId("Japan", LocalDate.of(2021, 3, 3));
        final CountryCovidDetails expectedCovidDetails = new CountryCovidDetails(covidDetailsId, 100, 1982);

        Mockito.when(covidDetailsRepository.findById(covidDetailsId)).thenReturn(Optional.of(expectedCovidDetails));

        Optional<CountryCovidDetails> actualCovidDetails = covidDetailsService.findById(covidDetailsId);
        Assertions.assertEquals(Optional.of(expectedCovidDetails), actualCovidDetails);
    }

    @Test
    void findByIdList() {

        final CountryCovidDetailsId covidDetailsId1 = new CountryCovidDetailsId("Japan", LocalDate.of(2021, 3, 3));
        final CountryCovidDetailsId covidDetailsId2 = new CountryCovidDetailsId("Belarus", LocalDate.of(2021, 3, 3));

        final CountryCovidDetails countryCovidDetails1 = new CountryCovidDetails(covidDetailsId1, 100, 200);
        final CountryCovidDetails countryCovidDetails2 = new CountryCovidDetails(covidDetailsId2, 300, 400);

        final CountryCovidDetailsDto expected1 = new CountryCovidDetailsDto("Japan", 100, 200, LocalDate.of(2021, 3, 3));
        final CountryCovidDetailsDto expected2 = new CountryCovidDetailsDto("Belarus", 300, 400, LocalDate.of(2021, 3, 3));

        Mockito.when(covidDetailsRepository.findById(covidDetailsId1)).thenReturn(Optional.of(countryCovidDetails1));
        Mockito.when(covidDetailsRepository.findById(covidDetailsId2)).thenReturn(Optional.of(countryCovidDetails2));
        Mockito.when(covidDetailsListMapper.mapToDtoList(List.of(countryCovidDetails1, countryCovidDetails2))).thenReturn(List.of(expected1, expected2));

        List<CountryCovidDetailsDto> covidDetailsDtoList = covidDetailsService.findByIdList(List.of(covidDetailsId1, covidDetailsId2));

        Assertions.assertTrue(covidDetailsDtoList.contains(expected1));
        Assertions.assertTrue(covidDetailsDtoList.contains(expected2));

        Mockito.verify(covidDetailsRepository, Mockito.times(2)).findById(Mockito.any(CountryCovidDetailsId.class));
    }

    @Test
    void ascertainCovidDetailsNotPresentedInDatabase() {

        List<CountryCovidDetailsDto> expectedNotPresentedCovidDetails = List.of(
                new CountryCovidDetailsDto("Belarus", 100, 200, LocalDate.now()),
                new CountryCovidDetailsDto("Japan", 100, 200, LocalDate.now())
        );

        List<String> notPresentingCountryList = List.of(
                "Belarus",
                "Japan"
        );

        List<CountryCovidDetailsDto> countryCovidDetailsFromApi = List.of(
                new CountryCovidDetailsDto("Belarus", 100, 200, LocalDate.now()),
                new CountryCovidDetailsDto("Japan", 100, 200, LocalDate.now()),
                new CountryCovidDetailsDto("China", 100, 200, LocalDate.now())
        );

        List<CountryCovidDetailsDto> actualNotPresentedCovidDetails = covidDetailsService.ascertainCovidDetailsNotPresentedInDatabase(notPresentingCountryList, countryCovidDetailsFromApi);

        Assertions.assertEquals(expectedNotPresentedCovidDetails, actualNotPresentedCovidDetails);
    }

    @Test
    void getCovidDetailsListForCountryListForTermFromDatabase() {
        List<String> countryList = List.of("Japan", "Belarus");
        final LocalDate firstDate = LocalDate.of(2021, 3, 13);
        final LocalDate lastDate = LocalDate.of(2021, 3, 17);

        final CountryCovidDetailsId firstDateCovidDetailsId1 = new CountryCovidDetailsId("Japan", LocalDate.of(2021, 3, 13));
        final CountryCovidDetailsId lastDateCovidDetailsId1 = new CountryCovidDetailsId("Japan", LocalDate.of(2021, 3, 17));
        final CountryCovidDetailsId firstDateCovidDetailsId2 = new CountryCovidDetailsId("Belarus", LocalDate.of(2021, 3, 13));
        final CountryCovidDetailsId lastDateCovidDetailsId2 = new CountryCovidDetailsId("Belarus", LocalDate.of(2021, 3, 17));

        final CountryCovidDetails firstDateCountryCovidDetails1 = new CountryCovidDetails(firstDateCovidDetailsId1, 100, 200);
        final CountryCovidDetails lastDateCountryCovidDetails1 = new CountryCovidDetails(lastDateCovidDetailsId1, 300, 400);
        final CountryCovidDetails firstDateCountryCovidDetails2 = new CountryCovidDetails(firstDateCovidDetailsId2, 100, 200);
        final CountryCovidDetails lastDateCountryCovidDetails2 = new CountryCovidDetails(lastDateCovidDetailsId2, 300, 400);

        List<CountryCovidDetailsDto> expectedCovidDetailsList = List.of(
                new CountryCovidDetailsDto("Japan", 200, 400, LocalDate.of(2021, 3, 13)),
                new CountryCovidDetailsDto("Belarus", 200, 400, LocalDate.of(2021, 3, 13))
        );

        Mockito.when(covidDetailsRepository.findById(firstDateCovidDetailsId1)).thenReturn(Optional.of(firstDateCountryCovidDetails1));
        Mockito.when(covidDetailsRepository.findById(lastDateCovidDetailsId1)).thenReturn(Optional.of(lastDateCountryCovidDetails1));
        Mockito.when(covidDetailsRepository.findById(firstDateCovidDetailsId2)).thenReturn(Optional.of(firstDateCountryCovidDetails2));
        Mockito.when(covidDetailsRepository.findById(lastDateCovidDetailsId2)).thenReturn(Optional.of(lastDateCountryCovidDetails2));

        List<CountryCovidDetailsDto> actualCovidDetailsList = covidDetailsService.getCovidDetailsListForCountryListForTermFromDatabase(countryList, firstDate, lastDate);
        Assertions.assertEquals(expectedCovidDetailsList, actualCovidDetailsList);
    }
}