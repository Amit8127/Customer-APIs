package com.driver.Dtos.ResponseDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnACustomerDto {

    // Data Transfer Object (DTO) representing the response for returning customer details

    // Important line: Lombok annotations @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor
    // provide automatic generation of getter, setter, builder pattern, and constructors,
    // reducing boilerplate code and enhancing code readability.

    private String first_name;
    private String last_name;
    private String street;
    private String address;
    private String city;
    private String state;
    private String phone;
    private String email;
}
