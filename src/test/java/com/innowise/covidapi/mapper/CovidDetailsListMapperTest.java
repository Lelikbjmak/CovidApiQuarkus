package com.innowise.covidapi.mapper;

import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.entity.CountryCovidDetails;
import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

@QuarkusTest
@RequiredArgsConstructor
class CovidDetailsListMapperTest {

    private final CovidDetailsListMapper covidDetailsListMapper;

    @Test
    void mapToEntityList() {

        List<CountryCovidDetails> countryCovidDetailsList = List.of(
                new CountryCovidDetails(new CountryCovidDetailsId("Belarus", LocalDate.now()), 1L, 2L),
                new CountryCovidDetails(new CountryCovidDetailsId("Bolivia", LocalDate.now()), 2L, 3L),
                new CountryCovidDetails(new CountryCovidDetailsId("Canada", LocalDate.now()), 3L, 4L)
        );

        List<CountryCovidDetailsDto> expectedDtoList = List.of(
                new CountryCovidDetailsDto("Belarus", 1L, 2L, LocalDate.now()),
                new CountryCovidDetailsDto("Bolivia", 2L, 3L, LocalDate.now()),
                new CountryCovidDetailsDto("Canada", 3L, 4L, LocalDate.now())
        );


        List<CountryCovidDetailsDto> actualDtoList = covidDetailsListMapper.mapToDtoList(countryCovidDetailsList);

        Assertions.assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    void mapToDtoList() {

        List<CountryCovidDetailsDto> covidDetailsDtoList = List.of(
                new CountryCovidDetailsDto("Belarus", 1L, 2L, LocalDate.now()),
                new CountryCovidDetailsDto("Bolivia", 2L, 3L, LocalDate.now()),
                new CountryCovidDetailsDto("Canada", 3L, 4L, LocalDate.now())
        );

        List<CountryCovidDetails> expectedEntityList = List.of(
                new CountryCovidDetails(new CountryCovidDetailsId("Belarus", LocalDate.now()), 1L, 2L),
                new CountryCovidDetails(new CountryCovidDetailsId("Bolivia", LocalDate.now()), 2L, 3L),
                new CountryCovidDetails(new CountryCovidDetailsId("Canada", LocalDate.now()), 3L, 4L)
        );

        List<CountryCovidDetails> actualEntityList = covidDetailsListMapper.mapToEntityList(covidDetailsDtoList);

        Assertions.assertEquals(expectedEntityList, actualEntityList);
    }
}