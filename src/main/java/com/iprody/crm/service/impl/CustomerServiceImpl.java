package com.iprody.crm.service.impl;

import com.iprody.crm.dto.CustomerDTO;
import com.iprody.crm.entity.ContactDetails;
import com.iprody.crm.entity.Country;
import com.iprody.crm.entity.Customer;
import com.iprody.crm.mapper.CustomerMapper;
import com.iprody.crm.repository.CustomerRepository;
import com.iprody.crm.service.ContactDetailsService;
import com.iprody.crm.service.CountryService;
import com.iprody.crm.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CountryService countryService;
    private final ContactDetailsService contactDetailsService;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Mono<CustomerDTO> save(CustomerDTO customerDTO) {
        log.info("trying to save a customer: {}", customerDTO);
        Mono<Country> foundCountryByIdMono = countryService.findById(customerDTO.getCountryDTO());
        Mono<ContactDetails> savedContactDetailsMono = contactDetailsService.save(customerDTO.getContactDetailsDTO());
        Customer customerToSave = customerMapper.toEntity(customerDTO);
        return Mono.zip(savedContactDetailsMono, foundCountryByIdMono)
                .flatMap(extract -> {
                    ContactDetails savedContactDetails = extract.getT1();
                    Country savedCountry = extract.getT2();
                    customerToSave.setContactDetails(savedContactDetails);
                    customerToSave.setCountry(savedCountry);
                    return Mono.fromCallable(() -> customerRepository.save(customerToSave))
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .doOnSuccess((savedCustomer) -> log.info("customer successfully saved {}", savedCustomer))
                .doOnError((error) -> log.error("Error when trying to save a customer: {}", customerDTO, error))
                .map(customerMapper::toDto);
    }
}
