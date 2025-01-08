package com.iprody.crm.mapper;

import com.iprody.crm.dto.CountryDTO;
import com.iprody.crm.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "customerList", ignore = true)
    Country toEntity(CountryDTO countryDTO);
    CountryDTO toDto(Country country);
}
