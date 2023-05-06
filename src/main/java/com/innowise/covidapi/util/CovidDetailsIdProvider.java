package com.innowise.covidapi.util;

import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.List;

@UtilityClass
public class CovidDetailsIdProvider {

    public static CountryCovidDetailsId getCovidDetailsId(String countryName, LocalDate date) {
        return new CountryCovidDetailsId(countryName, date);
    }

    public static List<CountryCovidDetailsId> getCovidDetailsIdListForCountryListForDate(List<String> countryNameList, LocalDate date) {
        return countryNameList.stream()
                .map(countryName -> getCovidDetailsId(countryName, date))
                .toList();
    }


}
