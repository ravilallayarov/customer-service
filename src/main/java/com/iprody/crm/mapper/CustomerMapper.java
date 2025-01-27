package com.iprody.crm.mapper;

import com.iprody.crm.dto.create.CustomerDTO;
import com.iprody.crm.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CountryMapper.class, ContactDetailsMapper.class})
public interface CustomerMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "country", source = "countryDTO")
    @Mapping(target = "contactDetails", source = "contactDetailsDTO")
    Customer toEntity(CustomerDTO customerDTO);
    @Mapping(target = "countryDTO", source = "country")
    @Mapping(target = "contactDetailsDTO", source = "contactDetails")
    CustomerDTO toDto(Customer customer);
}
