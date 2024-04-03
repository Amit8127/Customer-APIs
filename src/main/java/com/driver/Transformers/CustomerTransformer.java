package com.driver.Transformers;

import com.driver.Dtos.RequestDtos.CreateACustomerDto;
import com.driver.Dtos.ResponseDtos.ReturnACustomerDto;
import com.driver.Models.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class CustomerTransformer {

    public static Customer customerDtoToCustomer(CreateACustomerDto customerDto) {
        return Customer.builder()
                .uuid(UUID.randomUUID().toString())
                .first_name(customerDto.getFirst_name())
                .last_name(customerDto.getLast_name())
                .street(customerDto.getStreet())
                .address(customerDto.getAddress())
                .city(customerDto.getCity())
                .state(customerDto.getState())
                .email(customerDto.getEmail())
                .phone(customerDto.getPhone())
                .build();
    }


    public static ReturnACustomerDto customerToReturnACustomerDto(Customer customer) {
        return ReturnACustomerDto.builder()
                .first_name(customer.getFirst_name())
                .last_name(customer.getLast_name())
                .street(customer.getStreet())
                .address(customer.getAddress())
                .city(customer.getCity())
                .state(customer.getState())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }

    public static Customer convertJsonToCustomer(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, Customer.class);
    }
}
