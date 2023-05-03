package com.innowise.covidapi.mapper;

import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.entity.CountryCovidDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface CovidDetailsMapper {

    @Mapping(source = "id.date", target = "date")
    @Mapping(source = "id.country", target = "country")
    CountryCovidDetailsDto mapToDto(CountryCovidDetails countryCovidDetails);

    @Mapping(source = "date", target = "id.date")
    @Mapping(source = "country", target = "id.country")
    CountryCovidDetails mapToEntity(CountryCovidDetailsDto countryCovidDetailsDto);
}
