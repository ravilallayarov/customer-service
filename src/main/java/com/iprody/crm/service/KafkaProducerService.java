package com.iprody.crm.service;

import com.iprody.crm.dto.login.AuthRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, AuthRequestDTO> kafkaTemplate;

    public void sendMessage(AuthRequestDTO authRequestDTO) {
        kafkaTemplate.send("security", authRequestDTO);
        System.out.println("Отправили в топик " + authRequestDTO);
    }
}
