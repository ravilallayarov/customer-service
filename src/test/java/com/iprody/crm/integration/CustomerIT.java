package com.iprody.crm.integration;

import com.iprody.crm.repository.ContactDetailsRepository;
import com.iprody.crm.repository.CustomerRepository;
import org.junit.jupiter.api.*;
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

import static com.iprody.crm.testdata.TestObjectFactory.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@Testcontainers
public class CustomerIT {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ContactDetailsRepository contactDetailsRepository;
    @Autowired
    private WebTestClient webTestClient;
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

    /**
     * Очистка базы данных перед каждым тестом и сброс sequences
     */
    @BeforeEach
    public void cleanup() {
        customerRepository.deleteAll();
        customerRepository.resetAutoIncrement();
        contactDetailsRepository.resetAutoIncrement();
    }


    @Test
    public void save_customer_successfully() {
        String request = jsonRequestForCreateCustomer();

        WebTestClient.ResponseSpec customerResponse = webTestClient.post()
                .uri("/customers/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();
        checkCustomerResponse(customerResponse);
    }

    @Test
    public void findById_successfully_found() {
        String request = jsonRequestForCreateCustomer();
        webTestClient.post()
                .uri("/customers/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange();

        WebTestClient.ResponseSpec customerResponse = webTestClient.get()
                .uri(String.format("/customers/%s", ID))
                .exchange()
                .expectStatus().isOk();
        checkCustomerResponse(customerResponse);
    }

    @Test
    public void findById_throw_notFoundException() {
        webTestClient.get()
                .uri(String.format("/customers/%d", 2))
                .exchange()
                .expectStatus().isNotFound();
    }

    private void checkCustomerResponse(WebTestClient.ResponseSpec response) {
        response.expectBody()
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

    private String jsonRequestForCreateCustomer() {
        return String.format("""
                {
                   "name":"%s",
                   "surname":"%s",
                   "countryDTO":{
                      "id":%d
                   },
                   "contactDetailsDTO":{
                      "email":"%s",
                      "telegramId":"%s"
                   }
                }
                """, CUSTOMER_NAME, CUSTOMER_SURNAME, ID, EMAIL, TELEGRAM_ID);
    }
}
