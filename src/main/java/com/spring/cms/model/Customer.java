package com.spring.cms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {

    @JsonProperty("id")
    private int CustomerId;
    @JsonProperty("firstName")
    private String customerFirstName;
    @JsonProperty("lastName")
    private String customerLastName;
    @JsonProperty("email")
    private String customerEmail;

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
