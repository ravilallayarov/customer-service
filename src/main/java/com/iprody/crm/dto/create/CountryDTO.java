package com.iprody.crm.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CountryDTO {
    @NotNull(message = "country id cannot be null")
    private Long id;
    private UUID guid;
    private String countryCode;
    private String name;
}
