package com.innowise.covidapi.mapper;

import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.entity.CountryCovidDetails;
import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@QuarkusTest
@RequiredArgsConstructor
class CovidDetailsMapperTest {

    @Inject
    private final CovidDetailsMapper covidDetailsMapper;

    @Test
    void mapToDto() {

        CountryCovidDetails countryCovidDetails = new CountryCovidDetails(
                new CountryCovidDetailsId("Belarus", LocalDate.now()), 1L, 2L
        );

        CountryCovidDetailsDto expectedDto = new CountryCovidDetailsDto("Belarus", 1L, 2L, LocalDate.now());

        CountryCovidDetailsDto actualDto = covidDetailsMapper.mapToDto(countryCovidDetails);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    void mapToEntity() {

        CountryCovidDetailsDto countryCovidDetailsDto = new CountryCovidDetailsDto("Belarus", 1L, 2L, LocalDate.now());

        CountryCovidDetails expectedEntity = new CountryCovidDetails(
                new CountryCovidDetailsId("Belarus", LocalDate.now()), 1L, 2L
        );


        CountryCovidDetails actualEntity = covidDetailsMapper.mapToEntity(countryCovidDetailsDto);

        Assertions.assertEquals(expectedEntity, actualEntity);
    }
}