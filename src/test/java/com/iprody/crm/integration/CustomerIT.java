package com.iprody.crm.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@Testcontainers
public class CustomerIT {
    @Autowired
    private WebTestClient webTestClient;

    private static final String CUSTOMER_NAME = "Ravil";
    private static final String CUSTOMER_SURNAME = "Allayarov";
    private static final String COUNTRY_CODE = "RUS";
    private static final String COUNTRY_NAME = "Russia";
    private static final String EMAIL = "test@mail.ru";
    private static final String TELEGRAM_ID = "@test";
    private static final Long ID = 1L;

    @Container
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testDb")
            .withUsername("Ravil")
            .withPassword("Allayarov");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
    }


    @Test
    public void save_customer_successfully() {
        String request = jsonRequestForCreateCustomer();

        webTestClient.post()
                .uri("/customers/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(ID)
                .jsonPath("$.name").isEqualTo(CUSTOMER_NAME)
                .jsonPath("$.surname").isEqualTo(CUSTOMER_SURNAME)
                .jsonPath("$.countryDTO.id").isEqualTo(ID)
                .jsonPath("$.countryDTO.countryCode").isEqualTo(COUNTRY_CODE)
                .jsonPath("$.countryDTO.name").isEqualTo(COUNTRY_NAME)
                .jsonPath("$.contactDetailsDTO.id").isEqualTo(ID)
                .jsonPath("$.contactDetailsDTO.email").isEqualTo(EMAIL)
                .jsonPath("$.contactDetailsDTO.telegramId").isEqualTo(TELEGRAM_ID);
    }


    public String jsonRequestForCreateCustomer() {
        return """
                {
                   "name":"Ravil",
                   "surname":"Allayarov",
                   "countryDTO":{
                      "id":1
                   },
                   "contactDetailsDTO":{
                      "email":"test@mail.ru",
                      "telegramId":"@test"
                   }
                }
                """;
    }
}
