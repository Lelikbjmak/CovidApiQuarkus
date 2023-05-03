package com.innowise.covidapi.mapper;

import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.entity.Country;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
@RequiredArgsConstructor
class CountryMapperTest {

    private final CountryMapper countryMapper;

    @Test
    void mapToEntity() {
        CountryDto countryDto = new CountryDto("Belarus", "belarus", "BY");
        Country expectedCountry = new Country("belarus", "Belarus", "BY");

        Country actualCountry = countryMapper.mapToEntity(countryDto);

        Assertions.assertEquals(expectedCountry, actualCountry);
    }

    @Test
    void mapToDto() {
        Country country = new Country("belarus", "Belarus", "BY");
        CountryDto expected = new CountryDto("Belarus", "belarus", "BY");

        CountryDto actual = countryMapper.mapToDto(country);

        Assertions.assertEquals(expected, actual);
    }
}