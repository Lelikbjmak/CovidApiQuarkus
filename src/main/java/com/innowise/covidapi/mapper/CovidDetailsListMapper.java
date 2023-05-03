package com.innowise.covidapi.mapper;

import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.entity.CountryCovidDetails;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = CovidDetailsMapper.class)
public interface CovidDetailsListMapper {

    List<CountryCovidDetails> mapToEntityList(List<CountryCovidDetailsDto> covidDetailsDtoList);

    List<CountryCovidDetailsDto> mapToDtoList(List<CountryCovidDetails> covidDetailsDtoList);
}
