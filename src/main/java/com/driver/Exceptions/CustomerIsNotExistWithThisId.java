package com.driver.Exceptions;

public class CustomerIsNotExistWithThisId extends Exception{
    public CustomerIsNotExistWithThisId(Long id) {
        super("Customer Is Not Exist With This Id: " + id);
    }
}
