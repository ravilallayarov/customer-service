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

import java.util.List;

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
                .uri(String.format("/customers/%d", ID))
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

    @Test
    public void updateById_successfully_updated() {
        String requestForCreate = jsonRequestForCreateCustomer();
        String requestForUpdate = jsonRequestForUpdateCustomer();

        //create customer
        webTestClient.post()
                .uri("/customers/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestForCreate).exchange();

        //update customer
        webTestClient.post()
                .uri(String.format("/customers/%d/update", ID))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestForUpdate).exchange();

        webTestClient.get()
                .uri(String.format("/customers/%d", ID))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(NEW_CUSTOMER_NAME)
                .jsonPath("$.surname").isEqualTo(NEW_CUSTOMER_SURNAME);

    }

    @Test
    public void deleteById_successfully_deleted() {
        String requestForCreate = jsonRequestForCreateCustomer();

        //create customer
        webTestClient.post()
                .uri("/customers/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestForCreate)
                .exchange()
                .expectStatus().isCreated();

        //delete customer
        webTestClient.delete()
                .uri(String.format("/customers/%d/delete", ID))
                .exchange()
                .expectStatus().isNoContent();

        //trying to find customer after delete
        webTestClient.get()
                .uri(String.format("/customers/%d", ID))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getAll_finds_successfully() {
        createCustomers();

        webTestClient.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[*].id").value(ids -> {
                    List<Long> idsList = ((List<Integer>) ids).stream()
                            .map(Long::valueOf)
                            .toList();
                    for (int i = 0; i < idsList.size(); i++) {
                        Assertions.assertTrue(idsList.contains(ID + i));
                    }
                })
                .jsonPath("$[*].name").value(names -> {
                    List<String> nameList = (List<String>) names;
                    for (int i = 0; i < nameList.size(); i++) {
                        Assertions.assertTrue(nameList.contains(CUSTOMER_NAME + i));
                    }
                })
                .jsonPath("$[*].surname").value(surnames -> {
                    List<String> surnamesList = (List<String>) surnames;
                    for (int i = 0; i < surnamesList.size(); i++) {
                        Assertions.assertTrue(surnamesList.contains(CUSTOMER_SURNAME + i));
                    }
                })
                .jsonPath("$[*].contactDetailsDTO.email").value(emails -> {
                    List<String> emailList = (List<String>) emails;
                    for (int i = 0; i < emailList.size(); i++) {
                        Assertions.assertTrue(emailList.contains(i + EMAIL));
                    }
                })
                .jsonPath("$[*].contactDetailsDTO.telegramId").value(telegrams -> {
                    List<String> telegramsList = (List<String>) telegrams;
                    for (int i = 0; i < telegramsList.size(); i++) {
                        Assertions.assertTrue(telegramsList.contains(TELEGRAM_ID + i));
                    }
                });
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

    private String jsonRequestForUpdateCustomer() {
        return String.format("""
                {
                   "name":"%s",
                   "surname":"%s"
                }
                """, NEW_CUSTOMER_NAME, NEW_CUSTOMER_SURNAME);
    }

    private void createCustomers() {
        for (int i = 0; i < 5; i++) {
            String request = String.format("""
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
                    """, CUSTOMER_NAME + i, CUSTOMER_SURNAME + i, ID + i, i + EMAIL, TELEGRAM_ID + i);

            webTestClient.post()
                    .uri("/customers/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isCreated();
        }
    }
}
