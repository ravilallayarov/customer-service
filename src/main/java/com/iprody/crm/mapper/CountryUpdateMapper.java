package com.iprody.crm.mapper;

import com.iprody.crm.dto.update.CountryUpdateDTO;
import com.iprody.crm.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CountryUpdateMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "countryCode", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "customerList", ignore = true)
    void updateCountryFromDTO(CountryUpdateDTO countryUpdateDTO, @MappingTarget Country country);
}
