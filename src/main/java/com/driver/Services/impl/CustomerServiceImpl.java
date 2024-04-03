package com.driver.Services.impl;

import com.driver.Dtos.ResponseDtos.ReturnACustomerDto;
import com.driver.Exceptions.InvalidCustomerData;
import com.driver.Services.CustomerService;
import com.driver.Dtos.RequestDtos.CreateACustomerDto;
import com.driver.Exceptions.CustomerAlreadyExistWithThisEmail;
import com.driver.Exceptions.CustomerIsNotExistWithThisId;
import com.driver.Models.Customer;
import com.driver.Repositories.CustomerRepository;
import com.driver.Transformers.CustomerTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.driver.Transformers.CustomerTransformer.convertJsonToCustomer;


@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    String URL = "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";

    // Method to create a new customer
    @Override
    public Customer createACustomer(CreateACustomerDto createACustomerDto) throws CustomerAlreadyExistWithThisEmail, InvalidCustomerData {
        // Check if a customer with the provided email already exists
        if(customerRepository.findByEmail(createACustomerDto.getEmail()).isPresent()) {
            throw new CustomerAlreadyExistWithThisEmail(createACustomerDto.getEmail());
        }

        // Transform the DTO to a Customer entity
        Customer newCustomer = CustomerTransformer.customerDtoToCustomer(createACustomerDto);

        // data validation check
        boolean isCustomerObjValid = customerObjValidation(newCustomer);

        // Save the new customer to the repository
        if(isCustomerObjValid) {
            customerRepository.save(newCustomer);
        } else {
            throw new InvalidCustomerData();
        }

        return newCustomer;
    }

    // Method to update an existing customer by ID
    @Override
    public Customer updateACustomer(Long id, CreateACustomerDto updatedCustomer) throws CustomerIsNotExistWithThisId, InvalidCustomerData {
        // Check if a customer with the provided ID exists
        Optional<Customer> customerOptional = customerRepository.findById(id);

        if(customerOptional.isEmpty()) {
            throw new CustomerIsNotExistWithThisId(id);
        }

        // Retrieve the existing customer and update its details
        Customer existingCustomer = customerOptional.get();
        Customer newCustomer = CustomerTransformer.customerDtoToCustomer(updatedCustomer);
        newCustomer.setId(existingCustomer.getId());
        newCustomer.setUuid(existingCustomer.getUuid());
        // data validation check
        boolean isCustomerObjValid = customerObjValidation(newCustomer);

        // Save the new customer to the repository
        if(isCustomerObjValid) {
            customerRepository.save(newCustomer);
        } else {
            throw new InvalidCustomerData();
        }

        return newCustomer;
    }

    // Retrieves a paginated list of customers based on the provided parameters.
    @Override
    public List<Customer> getAllCustomersPages(int pageNum, int pageSize, Sort.Direction direction, String search) {

        // Create a PageRequest object to specify the page number, page size, sorting, and search criteria.
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, direction, search);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        // Retrieve the page of customers using the specified PageRequest.
        Page<Customer> customerPage;
        if (Objects.equals(search, "first_name")) {
            if(Objects.equals(direction.toString(), "ASC")) {
                customerPage = customerRepository.findByFirstNameASC(pageable);
            } else {
                customerPage = customerRepository.findByFirstNameDESC(pageable);
            }
        } else {
            customerPage= customerRepository.findAll( pageRequest);
        }

        // Extract the content (list of customers) from the page.
        List<Customer> customerList = customerPage.getContent();

        return customerList;
    }

    // Method to retrieve a customer by ID
    @Override
    public ReturnACustomerDto getCustomerById(Long id) throws CustomerIsNotExistWithThisId {
        // Check if a customer with the provided ID exists
        Optional<Customer> customerOptional = customerRepository.findById(id);

        if(customerOptional.isEmpty()) {
            throw new CustomerIsNotExistWithThisId(id);
        }

        // Transform the Customer entity to a DTO for response
        Customer existingCustomer = customerOptional.get();
        ReturnACustomerDto customerDto = CustomerTransformer.customerToReturnACustomerDto(existingCustomer);

        return customerDto;
    }

    // Method to delete a customer by ID
    @Override
    public String deleteACustomer(Long id) throws CustomerIsNotExistWithThisId {
        // Check if a customer with the provided ID exists
        Optional<Customer> customerOptional = customerRepository.findById(id);

        if(customerOptional.isEmpty()) {
            throw new CustomerIsNotExistWithThisId(id);
        }

        // Delete the customer from the repository
        customerRepository.deleteById(id);

        return "Customer Has Been Successfully Deleted";
    }

    // Method to get data from Sunbase API
    @Override
    public String getDataFromSunbase() throws Exception {
        try {
            // Set authorization header for Sunbase API request
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer dGVzdEBzdW5iYXNlZGF0YS5jb206VGVzdEAxMjM=");

            // Make a GET request to Sunbase API
            val response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<>(headers), List.class);
            List sunbaseCustomers = response.getBody();

            // Store the retrieved data into the local database
            return dataStoreFunction(sunbaseCustomers);

//            return "Data has been synced Successfully";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    // Function to store Sunbase data into the local database
    private String dataStoreFunction(List sunbaseCustomers) throws Exception {
        boolean invalidData = false;
        boolean validData = false;

        try {
            for(Object customer : sunbaseCustomers) {

                // Convert Sunbase customer data to JSON and then to Customer entity
                String obj = new ObjectMapper().writeValueAsString(customer);
                Customer customerObj = convertJsonToCustomer(obj);

                // Check if the customer already exists in the local database
                Optional<Customer> customerOptional = customerRepository.findByUuid(customerObj.getUuid());

                if(customerOptional.isEmpty()) {
                    // If not, save the customer to the database
                    boolean isObjDataValid = customerObjValidation(customerObj);
                    if(isObjDataValid) {
                        validData = true;
                        customerRepository.save(customerObj);
                    } else {
                        invalidData = true;
                    }
                } else {
                    // If exists, update the existing customer details
                    Customer fromDb = customerOptional.get();
                    customerObj.setId(fromDb.getId());
                    customerObj.setUuid(fromDb.getUuid());
                    customerRepository.save(customerObj);
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

        if(invalidData && validData) {
            return "Data has been synced Successfully but Error found in some data validation";
        } else if(invalidData) {
            return "Error found in data validation";
        }
        return "Data has been synced Successfully";
    }

    // Function to validate customer object
    private boolean customerObjValidation(Customer customerObj) {

        if(customerObj.getUuid().isEmpty()
                || customerObj.getFirst_name().isEmpty()
                || customerObj.getLast_name().isEmpty()
                || customerObj.getStreet().isEmpty()
                || customerObj.getAddress().isEmpty()
                || customerObj.getCity().isEmpty()
                || customerObj.getState().isEmpty()
                || customerObj.getEmail().isEmpty()
                || customerObj.getPhone().isEmpty()){
            return false;
        } else if(!isValidEmailAddress(customerObj.getEmail())) {
            System.out.println("Email :" + customerObj.getEmail());
            return false;
        } else if (!isValidPhoneNumber(customerObj.getPhone())){
            System.out.println("Phone :" + customerObj.getPhone());
            return false;
        }
        return true;
    }

    // Function to validate customer email
    public boolean isValidEmailAddress(String email) {
        String regex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Function to validate customer number
    public boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[1-9][0-9]{9}$"; // phoneNumber start with a digit from 1 to 9, followed by exactly 10 digits from 0 to 9.
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}
