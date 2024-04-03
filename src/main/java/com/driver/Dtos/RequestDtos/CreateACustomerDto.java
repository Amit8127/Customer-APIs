package com.driver.Dtos.RequestDtos;

import lombok.Data;

@Data
public class CreateACustomerDto {

    // Data Transfer Object (DTO) representing the request to create a new customer

    // Important line: Lombok annotation @Data generates boilerplate code for getter, setter, toString, and more.
    // It helps reduce code verbosity and improves code readability.

    private String first_name;
    private String last_name;
    private String street;
    private String address;
    private String city;
    private String state;
    private String phone;
    private String email;
}
