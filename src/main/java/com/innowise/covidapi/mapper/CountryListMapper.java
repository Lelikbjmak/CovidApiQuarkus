package com.innowise.covidapi.mapper;

import com.innowise.covidapi.dto.CountryDto;
import com.innowise.covidapi.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = CountryMapper.class)
public interface CountryListMapper {

    List<Country> mapToEntityList(List<CountryDto> dtoList);

    List<CountryDto> mapToDtoList(List<Country> entityList);
}
