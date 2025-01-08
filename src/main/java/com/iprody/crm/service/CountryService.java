package com.iprody.crm.service;

import com.iprody.crm.dto.CountryDTO;
import com.iprody.crm.entity.Country;
import reactor.core.publisher.Mono;

public interface CountryService {
    Mono<Country> findById(CountryDTO countryDTO);
}
