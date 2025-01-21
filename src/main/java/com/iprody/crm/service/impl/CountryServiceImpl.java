package com.iprody.crm.service.impl;

import com.iprody.crm.dto.CountryDTO;
import com.iprody.crm.entity.Country;
import com.iprody.crm.exception.NotFoundException;
import com.iprody.crm.repository.CountryRepository;
import com.iprody.crm.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public Mono<Country> findById(CountryDTO countryDTO) {
        log.info("trying to find a country by id: {}", countryDTO.getId());
        return Mono.fromCallable(() -> countryRepository.findById(countryDTO.getId()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(Mono::justOrEmpty)
                .doOnNext(country -> log.info("Country found: {}", country))
                .switchIfEmpty(Mono.error(() -> new NotFoundException("Country with this id " + countryDTO.getId() + " not found")))
                .doOnError(e -> log.info("Error when searching country by id: {}", countryDTO.getId(), e));
    }
}
