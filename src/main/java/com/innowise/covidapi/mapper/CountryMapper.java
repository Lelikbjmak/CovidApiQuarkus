package com.innowise.covidapi.mapper;

import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface CountryMapper {

    Country mapToEntity(CountryDto countryDto);

    CountryDto mapToDto(Country country);
}
