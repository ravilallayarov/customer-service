package com.iprody.crm.mapper;

import com.iprody.crm.dto.update.CustomerUpdateDTO;
import com.iprody.crm.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {ContactDetailsUpdateMapper.class, CountryUpdateMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface CustomerUpdateMapper {
    @Mapping(target = "country", source = "countryUpdateDTO")
    @Mapping(target = "contactDetails", source = "contactDetailsUpdateDTO")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "profileRef", ignore = true)
    void updateCustomerFromDTO(CustomerUpdateDTO customerUpdateDTO, @MappingTarget Customer customer);
}
