package com.driver.Controllers;

import com.driver.Dtos.RequestDtos.CreateACustomerDto;
import com.driver.Dtos.ResponseDtos.ReturnACustomerDto;
import com.driver.Models.Customer;
import com.driver.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Endpoint to trigger data synchronization from Sun base API
    @GetMapping("/getDataFromSunbase")
    public ResponseEntity<?> addAdmin() {
        try{
            // Call the service method to fetch and sync data from Sun base API
            String result = customerService.getDataFromSunbase();

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint for creating a new customer
    @PostMapping("/createACustomer")
    public ResponseEntity<?> createACustomer(@RequestBody CreateACustomerDto createACustomerDto) {
        try {
            // Call the service to create a new customer
            Customer result = customerService.createACustomer(createACustomerDto);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint for updating an existing customer by ID
    @PutMapping("/updateACustomer/{id}")
    public ResponseEntity<?> updateACustomer(@PathVariable Long id, @RequestBody CreateACustomerDto createACustomerDto) {
        try {
            // Call the service to update an existing customer
            Customer result = customerService.updateACustomer(id, createACustomerDto);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCustomerPages")
    public ResponseEntity<?> getAllCustomersPages(@RequestParam(defaultValue = "1") Integer pageNum,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                                  @RequestParam(defaultValue = "id") String search) {
        try {
            // Call the service to get all customers
            List<Customer> result = customerService.getAllCustomersPages(pageNum, pageSize, direction, search);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // Endpoint for retrieving a customer by ID
    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        try {
            // Call the service to get a customer by ID
            ReturnACustomerDto result = customerService.getCustomerById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint for deleting a customer by ID
    @DeleteMapping("/deleteACustomerById/{id}")
    public ResponseEntity<String> deleteACustomer(@PathVariable Long id) {
        try {
            // Call the service to delete a customer by ID
            String result = customerService.deleteACustomer(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
