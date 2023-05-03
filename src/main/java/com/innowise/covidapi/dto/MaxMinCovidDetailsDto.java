package com.innowise.covidapi.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MaxMinCovidDetailsDto {

    private List<CountryCovidDetailsDto> min;

    private List<CountryCovidDetailsDto> max;

}
