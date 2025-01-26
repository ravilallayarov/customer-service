package com.iprody.crm.unit;

import com.iprody.crm.dto.CustomerDTO;
import com.iprody.crm.entity.Customer;
import com.iprody.crm.exception.NotFoundException;
import com.iprody.crm.mapper.CustomerMapper;
import com.iprody.crm.repository.CustomerRepository;
import com.iprody.crm.service.ContactDetailsService;
import com.iprody.crm.service.CountryService;
import com.iprody.crm.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static com.iprody.crm.testdata.TestObjectFactory.*;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTest {
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private CountryService countryService;
    @Mock
    private ContactDetailsService contactDetailsService;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;


    @Test
    public void should_save_customer_successfully() {
        Mockito.when(countryService.findById(CUSTOMER_DTO_BEFORE_SAVE.getCountryDTO()))
                .thenReturn(Mono.just(COUNTRY_AFTER_SAVE));
        Mockito.when(contactDetailsService.save(CUSTOMER_DTO_BEFORE_SAVE.getContactDetailsDTO()))
                .thenReturn(Mono.just(CONTACT_DETAILS_AFTER_SAVE));
        Mockito.when(customerMapper.toEntity(CUSTOMER_DTO_BEFORE_SAVE))
                .thenReturn(CUSTOMER_BEFORE_SAVE);
        Mockito.when(customerRepository.save(CUSTOMER_BEFORE_SAVE))
                .thenReturn(CUSTOMER_AFTER_SAVE);
        Mockito.when(customerMapper.toDto(CUSTOMER_AFTER_SAVE))
                .thenReturn(CUSTOMER_DTO_EXPECTED);

        CustomerDTO customerDTOActual = customerService.save(CUSTOMER_DTO_BEFORE_SAVE).block();

        checkCustomerDTOExpectedAndCustomerDTOActual(CUSTOMER_DTO_EXPECTED, customerDTOActual);

        Mockito.verify(countryService).findById(CUSTOMER_DTO_BEFORE_SAVE.getCountryDTO());
        Mockito.verify(contactDetailsService).save(CUSTOMER_DTO_BEFORE_SAVE.getContactDetailsDTO());
        Mockito.verify(customerMapper).toEntity(CUSTOMER_DTO_BEFORE_SAVE);
        Mockito.verify(customerRepository).save(CUSTOMER_BEFORE_SAVE);
        Mockito.verify(customerMapper).toDto(CUSTOMER_AFTER_SAVE);
    }

    @Test
    public void should_be_thrown_an_exception_when_saving_the_customer() {
        Mockito.when(countryService.findById(CUSTOMER_DTO_BEFORE_SAVE.getCountryDTO()))
                .thenThrow(new NotFoundException("Country with this id "
                + CUSTOMER_DTO_BEFORE_SAVE.getCountryDTO().getId() + " not found"));

        Assertions.assertThrows(NotFoundException.class, () -> customerService.save(CUSTOMER_DTO_BEFORE_SAVE));
        Mockito.verify(countryService).findById(CUSTOMER_DTO_BEFORE_SAVE.getCountryDTO());
        Mockito.verifyNoInteractions(customerRepository);
    }

    @Test
    public void findById_should_return_customer() {
        Optional<Customer> customerDTOOptionalAfterSave = Optional.of(CUSTOMER_AFTER_SAVE);
        Mockito.doReturn(customerDTOOptionalAfterSave).when(customerRepository).findById(ID);
        Mockito.doReturn(CUSTOMER_DTO_EXPECTED).when(customerMapper).toDto(CUSTOMER_AFTER_SAVE);

        StepVerifier.create(customerService.findById(ID))
                .assertNext(customerDTOActual ->
                        checkCustomerDTOExpectedAndCustomerDTOActual(CUSTOMER_DTO_EXPECTED, customerDTOActual))
                .verifyComplete();

        Mockito.verify(customerRepository).findById(ID);
        Mockito.verify(customerMapper).toDto(CUSTOMER_AFTER_SAVE);
    }

    @Test
    public void findById_should_throw_notFoundException() {
        String errorMessage = "Customer with id: " + ID + " not found";
        Mockito.doThrow(new NotFoundException(errorMessage)).when(customerRepository).findById(ID);

        StepVerifier.create(customerService.findById(ID))
                .verifyErrorSatisfies(throwable -> {
                    Assertions.assertInstanceOf(NotFoundException.class, throwable);
                    Assertions.assertEquals(errorMessage, throwable.getMessage());
                });

        Mockito.verify(customerRepository).findById(ID);
    }

    private void checkCustomerDTOExpectedAndCustomerDTOActual(CustomerDTO expected, CustomerDTO actual) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getCountryDTO(), actual.getCountryDTO());
        Assertions.assertEquals(expected.getContactDetailsDTO(), actual.getContactDetailsDTO());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
    }
}
