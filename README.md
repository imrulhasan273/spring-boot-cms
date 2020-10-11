# spring-boot-cms

---

# Rest API CRUD Operation

---

## Model

`com.spring.cms/cms/model/Customer`

```java
package com.spring.cms.model;

public class Customer {
    
    private int CustomerId;
    private String customerFirstName;
    private String customerLastName;
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
```

- `columns` are defined using private variable above of the code segment.
- 

---

## Service

### Customer Service

`com.spring.cms/cms/service/CustomerService`

```java
package com.spring.cms.service;

import com.spring.cms.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CustomerService {

    private int customerIdCount = 1; 

    private List<Customer> customerList = new CopyOnWriteArrayList<>(); 

    public Customer addCustomer(Customer customer)
    {
        customer.setCustomerId(customerIdCount);
        customerList.add(customer);
        customerIdCount++;
        return customer;
    }

    public List<Customer> getCustomerList(){
        return customerList;
    }

    public Customer getCustomer(int customerId)
    {
        return customerList
                .stream()
                .filter(c -> c.getCustomerId() == customerId)
                .findFirst()
                .get();
    }

    public Customer updateCustomer(int customerId, Customer customer)
    {
        customerList
                .stream()
                .forEach(c->{
                    if(c.getCustomerId() == customerId)
                    {
                        c.setCustomerFirstName(customer.getCustomerFirstName());
                        c.setCustomerLastName(customer.getCustomerLastName());
                        c.setCustomerEmail(customer.getCustomerEmail());
                    }
                });

        return customerList
                .stream()
                .filter(c->c.getCustomerId() == customerId)
                .findFirst()
                .get();
    }

    public void deleteCustomer(int customerId)
    {
        customerList
                .stream()
                .forEach(c->{
                    if(c.getCustomerId() == customerId)
                    {
                        customerList.remove(c);
                    }
                });
    }
}
```

- This file contains all the business logic for the Customer Management System.
- `private int customerIdCount = 1;`
    - this variable is defined because we need a unique id for a customer.
    - We need save the id. But in the below function
    - say for example, in `addCustomer()` function we inject `Customer customer` as parameter. But ths `customer` contains info except `id`
    - So we generate ID from this file inside functions which is primarily defined using the private variable.
- `private List<Customer> customerList = new CopyOnWriteArrayList<>();`
    - this array `CopyOnWriteArrayList` type is used for concurrent operation.
    - This List contains all the Customer details.
- We use `@Component` to annoted the `CustomerService` class.
    - So that we can use this class inside another class and for that we need to annoted using `@Autowired` with reference class which is that class.

---

## API

### Customer Resource

`com.spring.cms/cms/api/CustomerResource`

```java
package com.spring.cms.api;

import com.spring.cms.model.Customer;
import com.spring.cms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/customers")
public class CustomerResource {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer)
    {
        return customerService.addCustomer(customer);
    }


    @GetMapping
    public List<Customer> getCustomers()
    {
        return customerService.getCustomerList();
    }

    @GetMapping(value = "/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") int customerId)
    {
        return customerService.getCustomer(customerId);
    }


    @PutMapping(value = "/{customerId}")
    public Customer updateCustomer(@PathVariable("customerId") int customerId, @RequestBody Customer customer)
    {
        return customerService.updateCustomer(customerId, customer);
    }

    @DeleteMapping(value = "/{customerId}")
    public void deleteCustomer(@PathVariable("customerId") int customerId)
    {
        customerService.deleteCustomer(customerId);
    }
}
```

- It will works like Controller.
- This controller is annotate by `@RestController`
- `private CustomerService customerService;`
    - Using that line we use the class `CustomerService` in this class using reference variable `customerService`
    - And inject it using `@Autowired` in this `Resource` class above the reference.
    - As we use the `@Autowired` we need to make sure. the reference class which is `CustomerService` class should be annotated with `@Component`
    
    
    
---

---
