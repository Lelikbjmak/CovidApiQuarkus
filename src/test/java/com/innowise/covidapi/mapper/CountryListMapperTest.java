package com.innowise.covidapi.mapper;

import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.entity.Country;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
@RequiredArgsConstructor
class CountryListMapperTest {

    private final CountryListMapper countryListMapper;

    @Test
    void mapToEntityList() {

        List<CountryDto> countryDtoList = List.of(
                new CountryDto("Belarus", "belarus", "BY"),
                new CountryDto("Japan", "japan", "JP")
        );

        List<Country> expectedCountryList = List.of(
                new Country("belarus", "Belarus", "BY"),
                new Country("japan", "Japan", "JP")
        );

        List<Country> actualCountryList = countryListMapper.mapToEntityList(countryDtoList);

        Assertions.assertEquals(expectedCountryList, actualCountryList);
    }

    @Test
    void mapToDtoList() {

        List<Country> countryList = List.of(
                new Country("belarus", "Belarus", "BY"),
                new Country("japan", "Japan", "JP")
        );


        List<CountryDto> expectedCountryDtoList = List.of(
                new CountryDto("Belarus", "belarus", "BY"),
                new CountryDto("Japan", "japan", "JP")
        );

        List<CountryDto> actualCountryDtoList = countryListMapper.mapToDtoList(countryList);

        Assertions.assertEquals(expectedCountryDtoList, actualCountryDtoList);
    }
}