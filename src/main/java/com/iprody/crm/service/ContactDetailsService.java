package com.iprody.crm.service;

import com.iprody.crm.dto.create.ContactDetailsDTO;
import com.iprody.crm.entity.ContactDetails;
import reactor.core.publisher.Mono;

public interface ContactDetailsService {
    Mono<ContactDetails> save(ContactDetailsDTO contactDetailsDTO);
    void checkIfContactDetailsExists(String email, String telegramId);
}
