package com.iprody.crm.service.impl;

import com.iprody.crm.dto.ContactDetailsDTO;
import com.iprody.crm.entity.ContactDetails;
import com.iprody.crm.exception.AlreadyExistException;
import com.iprody.crm.mapper.ContactDetailsMapper;
import com.iprody.crm.repository.ContactDetailsRepository;
import com.iprody.crm.service.ContactDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactDetailsServiceImpl implements ContactDetailsService {
    private final ContactDetailsRepository contactDetailsRepository;
    private final ContactDetailsMapper contactDetailsMapper;

    @Override
    public Mono<ContactDetails> save(ContactDetailsDTO contactDetailsDTO) {
        log.info("trying to save a contactDetails: {}", contactDetailsDTO);
        ContactDetails contactDetailsToSave = contactDetailsMapper.toEntity(contactDetailsDTO);
        String email = contactDetailsToSave.getEmail();
        String telegramId = contactDetailsToSave.getTelegramId();
        return Mono.fromCallable(() -> {
                    checkContactDetails(email, telegramId);
                    return contactDetailsRepository.save(contactDetailsToSave);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(savedContactDetails -> log.info("contactDetails successfully saved: {}", savedContactDetails))
                .doOnError(error -> log.error("Error when trying to save contactDetails: {}", contactDetailsToSave, error));
    }

    private void checkContactDetails(String email, String telegramId) {
        if (contactDetailsRepository.existsByEmail(email)) {
            throw new AlreadyExistException("Email already exists: " + email);
        }
        if (contactDetailsRepository.existsByTelegramId(telegramId)) {
            throw new AlreadyExistException("Telegram already exists: " + telegramId);
        }
    }
}
