package com.iprody.crm.mapper;

import com.iprody.crm.dto.create.ContactDetailsDTO;
import com.iprody.crm.entity.ContactDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactDetailsMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "customer", ignore = true)
    ContactDetails toEntity(ContactDetailsDTO contactDetailsDTO);
    ContactDetailsDTO toDto(ContactDetails contactDetails);
}
