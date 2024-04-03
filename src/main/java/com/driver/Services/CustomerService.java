package com.driver.Services;

import com.driver.Dtos.RequestDtos.CreateACustomerDto;
import com.driver.Dtos.ResponseDtos.ReturnACustomerDto;
import com.driver.Exceptions.CustomerAlreadyExistWithThisEmail;
import com.driver.Exceptions.CustomerIsNotExistWithThisId;
import com.driver.Exceptions.InvalidCustomerData;
import com.driver.Models.Customer;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CustomerService {

    Customer createACustomer(CreateACustomerDto createACustomerDto) throws Exception;

    Customer updateACustomer(Long id, CreateACustomerDto updatedCustomer) throws CustomerIsNotExistWithThisId, CustomerAlreadyExistWithThisEmail, InvalidCustomerData;

    List<Customer> getAllCustomersPages(int pageNum, int pageSize, Sort.Direction direction, String search);

    ReturnACustomerDto getCustomerById(Long id) throws CustomerIsNotExistWithThisId;

    String deleteACustomer(Long id) throws CustomerIsNotExistWithThisId;

    String getDataFromSunbase() throws Exception;
}
