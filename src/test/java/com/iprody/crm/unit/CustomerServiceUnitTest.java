package com.iprody.crm.unit;

import com.iprody.crm.dto.ContactDetailsDTO;
import com.iprody.crm.dto.CountryDTO;
import com.iprody.crm.dto.CustomerDTO;
import com.iprody.crm.entity.ContactDetails;
import com.iprody.crm.entity.Country;
import com.iprody.crm.entity.Customer;
import com.iprody.crm.exception.NotFoundException;
import com.iprody.crm.mapper.CustomerMapper;
import com.iprody.crm.repository.CustomerRepository;
import com.iprody.crm.service.ContactDetailsService;
import com.iprody.crm.service.CountryService;
import com.iprody.crm.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;


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

    private static final String CUSTOMER_NAME = "Ravil";
    private static final String CUSTOMER_SURNAME = "Allayarov";
    private static final String COUNTRY_CODE = "RUS";
    private static final String COUNTRY_NAME = "Russia";
    private static final String EMAIL = "test@mail.ru";
    private static final String TELEGRAM_ID = "@test";
    private static final Long ID = 1L;

    private static CustomerDTO customerDTOBeforeSave;
    private static CustomerDTO customerDTOExpected;
    private static Customer customerBeforeSave;
    private static Customer customerAfterSave;
    private static Country countryAfterSave;
    private static ContactDetails contactDetailsAfterSave;


    @Test
    public void should_save_customer_successfully() {
        Mockito.when(countryService.findById(customerDTOBeforeSave.getCountryDTO()))
                .thenReturn(Mono.just(countryAfterSave));
        Mockito.when(contactDetailsService.save(customerDTOBeforeSave.getContactDetailsDTO()))
                .thenReturn(Mono.just(contactDetailsAfterSave));
        Mockito.when(customerMapper.toEntity(customerDTOBeforeSave)).thenReturn(customerBeforeSave);
        Mockito.when(customerRepository.save(customerBeforeSave)).thenReturn(customerAfterSave);
        Mockito.when(customerMapper.toDto(customerAfterSave)).thenReturn(customerDTOExpected);

        CustomerDTO customerDTOActual = customerService.save(customerDTOBeforeSave).block();

        checkCustomerDTOExpectedAndCustomerDTOActual(customerDTOExpected, customerDTOActual);

        Mockito.verify(countryService).findById(customerDTOBeforeSave.getCountryDTO());
        Mockito.verify(contactDetailsService).save(customerDTOBeforeSave.getContactDetailsDTO());
        Mockito.verify(customerMapper).toEntity(customerDTOBeforeSave);
        Mockito.verify(customerRepository).save(customerBeforeSave);
        Mockito.verify(customerMapper).toDto(customerAfterSave);
    }

    @Test
    public void should_be_thrown_an_exception_when_saving_the_customer() {
        Mockito.when(countryService.findById(customerDTOBeforeSave.getCountryDTO()))
                .thenThrow(new NotFoundException("Country with this id "
                + customerDTOBeforeSave.getCountryDTO().getId() + " not found"));

        Assertions.assertThrows(NotFoundException.class, () -> customerService.save(customerDTOBeforeSave));
        Mockito.verify(countryService).findById(customerDTOBeforeSave.getCountryDTO());
        Mockito.verifyNoInteractions(customerRepository);
    }




    private void checkCustomerDTOExpectedAndCustomerDTOActual(CustomerDTO expected, CustomerDTO actual) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getCountryDTO(), actual.getCountryDTO());
        Assertions.assertEquals(expected.getContactDetailsDTO(), actual.getContactDetailsDTO());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
    }

    @BeforeAll
    public static void initAllObjects() {
        CountryDTO countryDTOBeforeSave = createCountryDTO(ID, null, null);
        CountryDTO countryDTOAfterSave = createCountryDTO(ID, COUNTRY_CODE, COUNTRY_NAME);

        ContactDetailsDTO contactDetailsDTOBeforeSave = createContactDetailsDTO(
                null, EMAIL, TELEGRAM_ID);
        ContactDetailsDTO contactDetailsDTOAfterSave = createContactDetailsDTO(
                ID, EMAIL, TELEGRAM_ID);

        customerDTOBeforeSave = createCustomerDTO(null, CUSTOMER_NAME, CUSTOMER_SURNAME,
                countryDTOBeforeSave, contactDetailsDTOBeforeSave);
        customerDTOExpected = createCustomerDTO(ID, CUSTOMER_NAME, CUSTOMER_SURNAME,
                countryDTOAfterSave, contactDetailsDTOAfterSave);

        Country countryBeforeSave = createCountry(ID, null, null);
        countryAfterSave = createCountry(ID, COUNTRY_CODE, COUNTRY_NAME);

        ContactDetails contactDetailsBeforeSave = createContactDetails(null, EMAIL, TELEGRAM_ID);
        contactDetailsAfterSave = createContactDetails(ID, EMAIL, TELEGRAM_ID);

        customerBeforeSave = createCustomer(null, CUSTOMER_NAME, CUSTOMER_SURNAME,
                countryBeforeSave, contactDetailsBeforeSave);
        customerAfterSave = createCustomer(ID, CUSTOMER_NAME, CUSTOMER_SURNAME,
                countryAfterSave, contactDetailsAfterSave);
    }

    private static CustomerDTO createCustomerDTO(Long id, String name, String surname,
                                                 CountryDTO countryDTO, ContactDetailsDTO contactDetailsDTO) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(id);
        customerDTO.setName(name);
        customerDTO.setSurname(surname);
        customerDTO.setCountryDTO(countryDTO);
        customerDTO.setContactDetailsDTO(contactDetailsDTO);
        return customerDTO;
    }

    private static Customer createCustomer(Long id, String name, String surname,
                                           Country country, ContactDetails contactDetails) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setSurname(surname);
        customer.setCountry(country);
        customer.setContactDetails(contactDetails);
        return customer;
    }

    private static CountryDTO createCountryDTO(Long id, String countryCode, String name) {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(id);
        countryDTO.setCountryCode(countryCode);
        countryDTO.setName(name);
        return countryDTO;
    }

    private static Country createCountry(Long id, String countryCode, String name) {
        Country country = new Country();
        country.setId(id);
        country.setCountryCode(countryCode);
        country.setName(name);
        return country;
    }

    private static ContactDetailsDTO createContactDetailsDTO(Long id, String email, String telegramId) {
        ContactDetailsDTO contactDetailsDTO = new ContactDetailsDTO();
        contactDetailsDTO.setId(id);
        contactDetailsDTO.setEmail(email);
        contactDetailsDTO.setTelegramId(telegramId);
        return contactDetailsDTO;
    }

    private static ContactDetails createContactDetails(Long id, String email, String telegramId) {
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setId(id);
        contactDetails.setEmail(email);
        contactDetails.setTelegramId(telegramId);
        return contactDetails;
    }
}
