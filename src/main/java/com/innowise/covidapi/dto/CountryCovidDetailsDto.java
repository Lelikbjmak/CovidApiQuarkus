package com.innowise.covidapi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CountryCovidDetailsDto {

    @JsonAlias(value = "Country")
    private String country;

    @JsonAlias(value = {"Cases", "NewConfirmed"})
    private long cases;

    @JsonAlias(value = {"Cases", "TotalConfirmed"})
    private long totalCases;

    @JsonAlias(value = "Date")
    private LocalDate date;
}
