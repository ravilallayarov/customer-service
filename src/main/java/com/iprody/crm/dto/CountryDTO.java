package com.iprody.crm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CountryDTO {
    @NotNull(message = "country id cannot be null")
    private Long id;
    private UUID guid;
    private String countryCode;
    private String name;
}
