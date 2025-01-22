package com.iprody.crm.testdata;

import com.iprody.crm.dto.ContactDetailsDTO;
import com.iprody.crm.dto.CountryDTO;
import com.iprody.crm.dto.CustomerDTO;
import com.iprody.crm.entity.ContactDetails;
import com.iprody.crm.entity.Country;
import com.iprody.crm.entity.Customer;

public class TestObjectFactory {
    public static final CountryDTO COUNTRY_DTO_BEFORE_SAVE;
    public static final CountryDTO COUNTRY_DTO_AFTER_SAVE;
    public static final Country COUNTRY_BEFORE_SAVE;
    public static final Country COUNTRY_AFTER_SAVE;
    public static final ContactDetailsDTO CONTACT_DETAILS_DTO_BEFORE_SAVE;
    public static final ContactDetailsDTO CONTACT_DETAILS_DTO_AFTER_SAVE;
    public static final ContactDetails CONTACT_DETAILS_BEFORE_SAVE;
    public static final ContactDetails CONTACT_DETAILS_AFTER_SAVE;
    public static final CustomerDTO CUSTOMER_DTO_BEFORE_SAVE;
    public static final CustomerDTO CUSTOMER_DTO_EXPECTED;
    public static final Customer CUSTOMER_BEFORE_SAVE;
    public static final Customer CUSTOMER_AFTER_SAVE;

    public static final String CUSTOMER_NAME = "Ravil";
    public static final String CUSTOMER_SURNAME = "Allayarov";
    public static final String COUNTRY_CODE = "RUS";
    public static final String COUNTRY_NAME = "Russia";
    public static final String EMAIL = "test@mail.ru";
    public static final String TELEGRAM_ID = "@test";
    public static final Long ID = 1L;

    static  {
        COUNTRY_DTO_BEFORE_SAVE = createCountryDTO(ID, null, null);
        COUNTRY_DTO_AFTER_SAVE = createCountryDTO(ID, COUNTRY_CODE, COUNTRY_NAME);
        COUNTRY_BEFORE_SAVE = createCountry(ID, null, null);
        COUNTRY_AFTER_SAVE = createCountry(ID, COUNTRY_CODE, COUNTRY_NAME);

        CONTACT_DETAILS_DTO_BEFORE_SAVE = createContactDetailsDTO(null, EMAIL, TELEGRAM_ID);
        CONTACT_DETAILS_DTO_AFTER_SAVE = createContactDetailsDTO(ID, EMAIL, TELEGRAM_ID);
        CONTACT_DETAILS_BEFORE_SAVE = createContactDetails(null, EMAIL, TELEGRAM_ID);
        CONTACT_DETAILS_AFTER_SAVE = createContactDetails(ID, EMAIL, TELEGRAM_ID);

        CUSTOMER_DTO_BEFORE_SAVE = createCustomerDTO(null, CUSTOMER_NAME, CUSTOMER_SURNAME,
                COUNTRY_DTO_BEFORE_SAVE, CONTACT_DETAILS_DTO_BEFORE_SAVE);
        CUSTOMER_DTO_EXPECTED = createCustomerDTO(ID, CUSTOMER_NAME, CUSTOMER_SURNAME,
                COUNTRY_DTO_AFTER_SAVE, CONTACT_DETAILS_DTO_AFTER_SAVE);
        CUSTOMER_BEFORE_SAVE = createCustomer(null, CUSTOMER_NAME, CUSTOMER_SURNAME,
                COUNTRY_BEFORE_SAVE, CONTACT_DETAILS_BEFORE_SAVE);
        CUSTOMER_AFTER_SAVE = createCustomer(ID, CUSTOMER_NAME, CUSTOMER_SURNAME,
                COUNTRY_AFTER_SAVE, CONTACT_DETAILS_AFTER_SAVE);
    }

    public static CustomerDTO createCustomerDTO(Long id, String name, String surname,
                                                CountryDTO countryDTO, ContactDetailsDTO contactDetailsDTO) {
        return CustomerDTO.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .countryDTO(countryDTO)
                .contactDetailsDTO(contactDetailsDTO)
                .build();
    }

    public static Customer createCustomer(Long id, String name, String surname,
                                          Country country, ContactDetails contactDetails) {
        return Customer.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .country(country)
                .contactDetails(contactDetails)
                .build();
    }

    public static CountryDTO createCountryDTO(Long id, String countryCode, String name) {
        return CountryDTO.builder()
                .id(id)
                .countryCode(countryCode)
                .name(name)
                .build();
    }

    public static Country createCountry(Long id, String countryCode, String name) {
        return Country.builder()
                .id(id)
                .countryCode(countryCode)
                .name(name)
                .build();
    }

    public static ContactDetailsDTO createContactDetailsDTO(Long id, String email, String telegramId) {
        return ContactDetailsDTO.builder()
                .id(id)
                .email(email)
                .telegramId(telegramId)
                .build();
    }

    public static ContactDetails createContactDetails(Long id, String email, String telegramId) {
        return ContactDetails.builder()
                .id(id)
                .email(email)
                .telegramId(telegramId)
                .build();
    }
}
