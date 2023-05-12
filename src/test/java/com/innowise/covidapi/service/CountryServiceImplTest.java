package com.innowise.covidapi.service;

import com.innowise.covidapi.entity.Country;
import com.innowise.covidapi.repository.CountryRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

@QuarkusTest
class CountryServiceImplTest {

    @InjectMock
    private CountryRepository countryRepository;

    @Inject
    private CountryService countryService;

    @Test
    void saveAll() {
        List<Country> countryList = List.of(
                new Country("japan", "Japan", "JP")
        );
        countryService.saveAll(countryList);
        Mockito.verify(countryRepository, Mockito.times(1)).persist(countryList);
    }

    @Test
    void findByName() {
        Optional<Country> expectedCountry = Optional.of(new Country("japan", "Japan", "JP"));
        Mockito.when(countryRepository.findByName("Japan")).thenReturn(expectedCountry);

        Optional<Country> actualCountry = countryService.findByName("Japan");
        Assertions.assertEquals(expectedCountry, actualCountry);
        Mockito.verify(countryRepository, Mockito.times(1)).findByName("Japan");
    }

    @Test
    void getSlugListByCountryNameList() {

        Optional<Country> expectedCountry = Optional.of(new Country("japan", "Japan", "JP"));
        Mockito.when(countryRepository.findByName("Japan")).thenReturn(expectedCountry);

        List<String> countrySlugList = countryService.getSlugListByCountryNameList(List.of("Japan"));

        Assertions.assertTrue(countrySlugList.contains("japan"));
        Mockito.verify(countryRepository, Mockito.times(1)).findByName("Japan");
    }
}