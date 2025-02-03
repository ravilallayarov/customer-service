package com.iprody.crm.unit;

import com.iprody.crm.dto.create.CustomerDTO;
import com.iprody.crm.dto.getAll.RequestForGetAllCustomers;
import com.iprody.crm.dto.update.CustomerUpdateDTO;
import com.iprody.crm.entity.Customer;
import com.iprody.crm.exception.NotFoundException;
import com.iprody.crm.mapper.CustomerMapper;
import com.iprody.crm.mapper.CustomerUpdateMapper;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static com.iprody.crm.testdata.TestObjectFactory.*;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTest {
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private CustomerUpdateMapper customerUpdateMapper;
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

        checkCustomerExpectedAndCustomerActual(CUSTOMER_DTO_EXPECTED, customerDTOActual);

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
        Optional<Customer> customerOptionalAfterSave = Optional.of(CUSTOMER_AFTER_SAVE);
        Mockito.doReturn(customerOptionalAfterSave).when(customerRepository).findById(ID);

        StepVerifier.create(customerService.findById(ID))
                .assertNext(customerActual ->
                        checkCustomerExpectedAndCustomerActual(CUSTOMER_AFTER_SAVE, customerActual))
                .verifyComplete();

        Mockito.verify(customerRepository).findById(ID);
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

    @Test
    public void updateById_should_update_customer() {
        Optional<Customer> customerById = Optional.of(CUSTOMER_AFTER_SAVE);
        Customer customerExpected = CUSTOMER_AFTER_UPDATE;

        Mockito.doReturn(customerById).when(customerRepository).findById(ID);
        Mockito.doAnswer(invocationOnMock -> {
            CustomerUpdateDTO customerUpdateDTO = invocationOnMock.getArgument(0);
            Customer customer = invocationOnMock.getArgument(1);
            customer.setName(customerUpdateDTO.getName());
            customer.setSurname(customerUpdateDTO.getSurname());
            return null;
        }).when(customerUpdateMapper).updateCustomerFromDTO(CUSTOMER_UPDATE_DTO, customerById.get());
        Mockito.doReturn(customerExpected).when(customerRepository).save(customerById.get());

        StepVerifier.create(customerService.updateById(ID, CUSTOMER_UPDATE_DTO))
                .assertNext(customerActual -> {
                    Assertions.assertEquals(customerExpected.getName(), customerActual.getName());
                    Assertions.assertEquals(customerExpected.getSurname(), customerActual.getSurname());
                })
                .verifyComplete();

        Mockito.verify(customerRepository).findById(ID);
        Mockito.verify(customerUpdateMapper).updateCustomerFromDTO(CUSTOMER_UPDATE_DTO, customerById.get());
        Mockito.verify(customerRepository).save(customerById.get());
    }

    @Test
    public void deleteById_should_delete_customer() {
        Optional<Customer> customerById = Optional.of(CUSTOMER_AFTER_SAVE);

        Mockito.doReturn(customerById).when(customerRepository).findById(ID);
        Mockito.doNothing().when(customerRepository).deleteById(ID);

        StepVerifier.create(customerService.deleteById(ID))
                .verifyComplete();

        Mockito.verify(customerRepository).findById(ID);
        Mockito.verify(customerRepository).deleteById(ID);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getAll_should_return_all_customers() {
        RequestForGetAllCustomers request = new RequestForGetAllCustomers();
        List<Customer> expected = createCustomersListAfterSave();
        PageImpl<Customer> page = new PageImpl<>(expected);

        Mockito.doReturn(page).when(customerRepository).findAll(Mockito.any(Specification.class), Mockito.any(PageRequest.class));

        StepVerifier.create(customerService.getAll(request))
                .assertNext((actual) -> {
                    for (int i = 0; i < actual.size(); i++) {
                        checkCustomerExpectedAndCustomerActual(expected.get(i), actual.get(i));
                    }
                })
                .verifyComplete();

        Mockito.verify(customerRepository).findAll(Mockito.any(Specification.class), Mockito.any(PageRequest.class));
    }

    private void checkCustomerExpectedAndCustomerActual(CustomerDTO expected, CustomerDTO actual) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getCountryDTO(), actual.getCountryDTO());
        Assertions.assertEquals(expected.getContactDetailsDTO(), actual.getContactDetailsDTO());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
    }

    private void checkCustomerExpectedAndCustomerActual(Customer expected, Customer actual) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getCountry(), actual.getCountry());
        Assertions.assertEquals(expected.getContactDetails(), actual.getContactDetails());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
    }
}
