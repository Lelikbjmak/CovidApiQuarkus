package com.innowise.covidapi.service;

import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.dto.MaxMinCovidDetailsDto;
import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import com.innowise.covidapi.mapper.CovidDetailsMapper;
import com.innowise.covidapi.service.impl.ApiResponseParserService;
import com.innowise.covidapi.service.impl.CountryCovidDetailsRedisService;
import com.innowise.covidapi.util.CovidDetailsIdProvider;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@QuarkusTest
class MaxMinCovidDetailsEvaluatorServiceImplTest {

    @Inject
    private MaxMinCovidDetailsEvaluatorService maxMinCovidDetailsEvaluatorService;

    @InjectMock
    private CovidDetailsService covidDetailsService;

    @InjectMock
    private CovidDetailsMapper covidDetailsMapper;

    @InjectMock
    private CountryService countryService;

    @InjectMock
    private ApiResponseParserService apiResponseParserService;

    @InjectMock
    private CountryCovidDetailsRedisService countryCovidDetailsRedisService;

    @Test
    @DisplayName("Covid details for today. (Not all CovidDetails found in Database)")
    void getPartlyCovidDetailsForToday() {
        final List<String> countryList = new ArrayList<>(List.of("Belarus", "Japan"));
        final List<CountryCovidDetailsId> covidDetailsIdList =
                CovidDetailsIdProvider.getCovidDetailsIdListForCountryListForDate(countryList, LocalDate.now());
        final List<String> remainCountryList = List.of("Belarus");
        final List<CountryCovidDetailsDto> todayCovidDetailsFromApi = List.of(
                new CountryCovidDetailsDto("Belarus", 45, 366, LocalDate.now()),
                new CountryCovidDetailsDto("Columbia", 45, 366, LocalDate.now()),
                new CountryCovidDetailsDto("Russia", 45, 366, LocalDate.now())
        );

        Mockito.when(countryCovidDetailsRedisService.findByIdList(covidDetailsIdList)).thenReturn(List.of(
                new CountryCovidDetailsDto("Japan", 200, 689, LocalDate.now())
        ));

        Mockito.when(apiResponseParserService.getTodayCovidDetailsFromApi()).thenReturn(todayCovidDetailsFromApi);
        Mockito.when(covidDetailsService.ascertainCovidDetailsNotPresentedInDatabase(remainCountryList, todayCovidDetailsFromApi))
                .thenReturn(List.of(new CountryCovidDetailsDto("Belarus", 45, 366, LocalDate.now())));

        MaxMinCovidDetailsDto todayMaxMinCovidDetails = maxMinCovidDetailsEvaluatorService.getCovidDetailsForToday(countryList);

        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("Belarus", 45, 366, LocalDate.now())),
                todayMaxMinCovidDetails.getMin());

        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("Japan", 200, 689, LocalDate.now())),
                todayMaxMinCovidDetails.getMax());

        Mockito.verify(covidDetailsService, Mockito.times(1)).ascertainCovidDetailsNotPresentedInDatabase(remainCountryList, todayCovidDetailsFromApi);
        Mockito.verify(countryCovidDetailsRedisService, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(covidDetailsMapper, Mockito.times(1)).mapToEntity(Mockito.any());
        Mockito.verify(apiResponseParserService, Mockito.times(1)).getTodayCovidDetailsFromApi();
    }

    @Test
    @DisplayName("CovidDetailsForToday. (All CovidDetails found in Database)")
    void getAllCovidDetailsForToday() {
        final List<String> countryList = new ArrayList<>(List.of("Belarus", "Japan"));
        final List<CountryCovidDetailsId> covidDetailsIdList =
                CovidDetailsIdProvider.getCovidDetailsIdListForCountryListForDate(countryList, LocalDate.now());

        Mockito.when(countryCovidDetailsRedisService.findByIdList(covidDetailsIdList)).thenReturn(List.of(
                new CountryCovidDetailsDto("Japan", 289, 786, LocalDate.now()),
                new CountryCovidDetailsDto("Belarus", 89, 346, LocalDate.now())
        ));

        MaxMinCovidDetailsDto todayMaxMinCovidDetails = maxMinCovidDetailsEvaluatorService.getCovidDetailsForToday(countryList);

        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("Japan", 289, 786, LocalDate.now())),
                todayMaxMinCovidDetails.getMax());
        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("Belarus", 89, 346, LocalDate.now())),
                todayMaxMinCovidDetails.getMin());

        Mockito.verify(covidDetailsService, Mockito.times(0)).ascertainCovidDetailsNotPresentedInDatabase(Mockito.any(), Mockito.any());
        Mockito.verify(countryCovidDetailsRedisService, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(covidDetailsService, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(covidDetailsMapper, Mockito.times(0)).mapToEntity(Mockito.any());
        Mockito.verify(covidDetailsMapper, Mockito.times(0)).mapToDto(Mockito.any());
        Mockito.verify(apiResponseParserService, Mockito.times(0)).getTodayCovidDetailsFromApi();
    }

    @Test
    @DisplayName("Covid details for certain day. (Not all details found in DB)")
    void getPartlyCovidDetailsForCertainDay() {
        final LocalDate date = LocalDate.of(2021, 3, 14);
        final List<String> countryList = new ArrayList<>(List.of("Belarus", "Japan", "Bolivia"));
        final List<CountryCovidDetailsId> covidDetailsIdList =
                CovidDetailsIdProvider.getCovidDetailsIdListForCountryListForDate(countryList, date);

        final List<CountryCovidDetailsDto> covidDetailsFromApi = List.of(
                new CountryCovidDetailsDto("Belarus", 10, 133, date),
                new CountryCovidDetailsDto("USA", 123, 223, date),
                new CountryCovidDetailsDto("Bolivia", 40, 824, date)
        );

        Mockito.when(covidDetailsService.findByIdList(covidDetailsIdList)).thenReturn(List.of(
                new CountryCovidDetailsDto("Japan", 200, 689, date)
        ));

        Mockito.when(countryService.getSlugListByCountryNameList(List.of("Belarus", "Bolivia")))
                .thenReturn(List.of("belarus", "bolivia"));

        Mockito.when(apiResponseParserService.getTermCovidDetailsFromApi(List.of("belarus", "bolivia"), date, date.plusDays(2)))
                .thenReturn(covidDetailsFromApi);

        Mockito.when(covidDetailsService.ascertainCovidDetailsNotPresentedInDatabase(List.of("Belarus", "Bolivia"), covidDetailsFromApi))
                .thenReturn(List.of(
                        new CountryCovidDetailsDto("Belarus", 10, 133, date),
                        new CountryCovidDetailsDto("Bolivia", 40, 824, date)
                ));

        MaxMinCovidDetailsDto certainDayCovidDetails = maxMinCovidDetailsEvaluatorService.getCovidDetailsForCertainDay(countryList, date);

        Assertions.assertNotNull(certainDayCovidDetails);
        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("Belarus", 10, 133, date)), certainDayCovidDetails.getMin());
        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("Japan", 200, 689, date)), certainDayCovidDetails.getMax());

        Mockito.verify(covidDetailsService, Mockito.times(1)).findByIdList(Mockito.any());
        Mockito.verify(covidDetailsService, Mockito.times(2)).save(Mockito.any());
        Mockito.verify(countryService, Mockito.times(1)).getSlugListByCountryNameList(Mockito.any());
        Mockito.verify(apiResponseParserService, Mockito.times(1)).getTermCovidDetailsFromApi(List.of("belarus", "bolivia"), date, date.plusDays(2));
        Mockito.verify(covidDetailsService, Mockito.times(1)).ascertainCovidDetailsNotPresentedInDatabase(List.of("Belarus", "Bolivia"), covidDetailsFromApi);
    }

    @Test
    @DisplayName("Covid details for certain day. (All details found in DB)")
    void getAllCovidDetailsForCertainDay() {
        final LocalDate date = LocalDate.of(2021, 3, 14);
        final List<String> countryList = new ArrayList<>(List.of("Belarus", "Japan", "Bolivia"));
        final List<CountryCovidDetailsId> covidDetailsIdList =
                CovidDetailsIdProvider.getCovidDetailsIdListForCountryListForDate(countryList, date);

        final List<CountryCovidDetailsDto> covidDetailsDtoList = List.of(
                new CountryCovidDetailsDto("Belarus", 10, 133, date),
                new CountryCovidDetailsDto("USA", 123, 223, date),
                new CountryCovidDetailsDto("Bolivia", 40, 824, date)
        );

        Mockito.when(covidDetailsService.findByIdList(covidDetailsIdList)).thenReturn(covidDetailsDtoList);

        MaxMinCovidDetailsDto certainDayCovidDetails = maxMinCovidDetailsEvaluatorService.getCovidDetailsForCertainDay(countryList, date);

        Assertions.assertNotNull(certainDayCovidDetails);
        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("USA", 123, 223, date)),
                certainDayCovidDetails.getMax());
        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("Belarus", 10, 133, date)),
                certainDayCovidDetails.getMin());

        Mockito.verify(covidDetailsService, Mockito.times(1)).findByIdList(covidDetailsIdList);
        Mockito.verify(countryService, Mockito.times(0)).getSlugListByCountryNameList(Mockito.any());
        Mockito.verify(covidDetailsService, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(apiResponseParserService, Mockito.times(0)).getTermCovidDetailsFromApi(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(covidDetailsService, Mockito.times(0)).ascertainCovidDetailsNotPresentedInDatabase(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Covid details for term. (Not all details found in DB)")
    void getCovidDetailsForTerm() {
        final LocalDate firstDate = LocalDate.of(2021, 3, 14);
        final LocalDate lastDate = LocalDate.of(2021, 3, 17);
        final List<String> countryList = new ArrayList<>(List.of("Belarus", "Japan", "Bolivia"));

        final List<CountryCovidDetailsDto> covidDetailsFromApi = List.of(
                new CountryCovidDetailsDto("Belarus", 10, 133, firstDate),
                new CountryCovidDetailsDto("USA", 123, 223, firstDate),
                new CountryCovidDetailsDto("Bolivia", 40, 824, firstDate)
        );

        Mockito.when(covidDetailsService.getCovidDetailsListForCountryListForTermFromDatabase(countryList, firstDate, lastDate)).thenReturn(List.of(
                new CountryCovidDetailsDto("Japan", 200, 689, firstDate)
        ));

        Mockito.when(countryService.getSlugListByCountryNameList(List.of("Belarus", "Bolivia")))
                .thenReturn(List.of("belarus", "bolivia"));

        Mockito.when(apiResponseParserService.getTermCovidDetailsFromApi(List.of("belarus", "bolivia"), firstDate, lastDate.plusDays(2)))
                .thenReturn(covidDetailsFromApi);

        Mockito.when(covidDetailsService.ascertainCovidDetailsNotPresentedInDatabase(List.of("Belarus", "Bolivia"), covidDetailsFromApi))
                .thenReturn(List.of(
                        new CountryCovidDetailsDto("Belarus", 10, 133, firstDate),
                        new CountryCovidDetailsDto("Bolivia", 40, 824, firstDate)
                ));

        MaxMinCovidDetailsDto certainDayCovidDetails = maxMinCovidDetailsEvaluatorService.getCovidDetailsForTerm(countryList, firstDate, lastDate);

        Assertions.assertNotNull(certainDayCovidDetails);
        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("Belarus", 10, 133, firstDate)), certainDayCovidDetails.getMin());
        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("Japan", 200, 689, firstDate)), certainDayCovidDetails.getMax());

        Mockito.verify(covidDetailsService, Mockito.times(1)).getCovidDetailsListForCountryListForTermFromDatabase(countryList, firstDate, lastDate);
        Mockito.verify(countryService, Mockito.times(1)).getSlugListByCountryNameList(Mockito.any());
        Mockito.verify(apiResponseParserService, Mockito.times(1)).getTermCovidDetailsFromApi(List.of("belarus", "bolivia"), firstDate, lastDate.plusDays(2));
        Mockito.verify(covidDetailsService, Mockito.times(1)).ascertainCovidDetailsNotPresentedInDatabase(List.of("Belarus", "Bolivia"), covidDetailsFromApi);
    }

    @Test
    @DisplayName("Covid details for term. (All details found in DB)")
    void getAllCovidDetailsForTerm() {
        final LocalDate firstDate = LocalDate.of(2021, 3, 14);
        final LocalDate lastDate = LocalDate.of(2021, 3, 17);
        final List<String> countryList = new ArrayList<>(List.of("Belarus", "Japan", "Bolivia"));

        final List<CountryCovidDetailsDto> covidDetailsDtoList = List.of(
                new CountryCovidDetailsDto("Belarus", 10, 133, firstDate),
                new CountryCovidDetailsDto("Japan", 123, 223, firstDate),
                new CountryCovidDetailsDto("Bolivia", 40, 824, firstDate)
        );

        Mockito.when(covidDetailsService.getCovidDetailsListForCountryListForTermFromDatabase(countryList, firstDate, lastDate)).thenReturn(covidDetailsDtoList);

        MaxMinCovidDetailsDto certainDayCovidDetails = maxMinCovidDetailsEvaluatorService.getCovidDetailsForTerm(countryList, firstDate, lastDate);

        Assertions.assertNotNull(certainDayCovidDetails);
        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("Japan", 123, 223, firstDate)),
                certainDayCovidDetails.getMax());
        Assertions.assertEquals(List.of(new CountryCovidDetailsDto("Belarus", 10, 133, firstDate)),
                certainDayCovidDetails.getMin());

        Mockito.verify(covidDetailsService, Mockito.times(1)).getCovidDetailsListForCountryListForTermFromDatabase(countryList, firstDate, lastDate);
        Mockito.verify(countryService, Mockito.times(0)).getSlugListByCountryNameList(Mockito.any());
        Mockito.verify(covidDetailsService, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(apiResponseParserService, Mockito.times(0)).getTermCovidDetailsFromApi(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(covidDetailsService, Mockito.times(0)).ascertainCovidDetailsNotPresentedInDatabase(Mockito.any(), Mockito.any());
    }
}