package com.iprody.crm.mapper;

import com.iprody.crm.dto.update.ContactDetailsUpdateDTO;
import com.iprody.crm.entity.ContactDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContactDetailsUpdateMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateContactDetailsFromDTO(ContactDetailsUpdateDTO contactDetailsUpdateDTO, @MappingTarget ContactDetails contactDetails);
}
