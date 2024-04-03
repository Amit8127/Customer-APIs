package com.driver.Exceptions;

public class CustomerAlreadyExistWithThisEmail extends Exception{
    public CustomerAlreadyExistWithThisEmail(String email) {
        super("Customer Already Exist With This EmailId: " + email);
    }
}
