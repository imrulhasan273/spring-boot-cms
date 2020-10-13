# spring-boot-cms

---

# Rest API CRUD Operation

---

## Model

`com.spring.cms/cms/model/Customer`

```java
package com.spring.cms.model;

public class Customer {
    
    private int customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

### Customer Controller

`com.spring.cms/cms/ApiController/CustomerController`

```java
package com.spring.cms.ApiController;

import com.spring.cms.model.Customer;
import com.spring.cms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

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

# **@JsonProperty**

---

- When client use the API to run POST,GET,PUT,DELETE etc operation in my project, they use the same column name that are defined in our `Model`
- But I don't want to expose the real column names to the client.
- In that case we need `@JsonProperty`

```java
public class Customer
{
    @JsonProperty("id")
    private int CustomerId;
    
    @JsonProperty("firstName")
    private String customerFirstName;
    
    @JsonProperty("lastName")
    private String customerLastName;
    
    @JsonProperty("email")
    private String customerEmail;
}
```
- Here `CustomerId` is called by `id` in Client API.

---

---

# **Add a Database**

---

Add e new dependency in `pom.xml`

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

Added the H2 DataBase Engine Dependency 

```xml
<!-- https://mvnrepository.com/artifact/com.h2database/h2 -->
<dependency>
    <groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<version>1.4.200</version>
	<scope>test</scope>
</dependency>
```

---

# **CRUD Operation using DB | Configuration**

---

## Convert the `Customer` model class to Entity.

```java

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private int customerId;

    @JsonProperty("firstName")
    private String customerFirstName;

    @JsonProperty("lastName")
    private String customerLastName;

    @JsonProperty("email")
    private String customerEmail;
}
```

- `@Entity`
- `@Id`
    - to define primary key as `ID`
- `@GeneratedValue(strategy = GenerationType.AUTO)`
    - to set primary key `Auto Increamenting`

## Create a new interface

`dao/CustomerDAO.java`

```java
package com.spring.cms.dao;

import com.spring.cms.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDAO extends CrudRepository<Customer, Integer> {
    //
}
```

## Main Class

`CmsApplication.java`

```java
package com.spring.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CmsApplication {

	public static void main(String[] args) {

		SpringApplication.run(CmsApplication.class, args);
	}
}

```
- Add `@EnableJpaRepositories` in this main class.

---

---

# **CRUD Operation | H2 DB | RestAPI**

---

`com.spring.cms/dao/CustomerDAO.java`

```java
package com.spring.cms.dao;

import com.spring.cms.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDAO extends CrudRepository<Customer, Integer> {

    @Override
    List<Customer> findAll();
}
```

`com.spring.cms/service/CustomerService.java`

```java
package com.spring.cms.service;

import com.spring.cms.dao.CustomerDAO;
import com.spring.cms.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerService {


    @Autowired
    private CustomerDAO customerDAO;

    public Customer addCustomer(Customer customer)
    {
        return customerDAO.save(customer);
    }

    public List<Customer> getCustomerList(){
        return customerDAO.findAll();
    }

    public Customer getCustomer(int customerId)
    {
        return customerDAO.findById(customerId).get();
    }

    public Customer updateCustomer(int customerId, Customer customer)
    {
        customer.setCustomerId(customerId);
        return customerDAO.save(customer);

    }

    public void deleteCustomer(int customerId)
    {
        customerDAO.deleteById(customerId);
    }
}
```

- First we `@Autowired` CustomerDAO class with reference variable.
- In `addCustomer(Customer customer)` we simply insert a data using `save()` method.
- In `getCustomerList()` method we want to GET a list of Customer.
    - But we use `customerDAO.findAll();` to get the customers.
        - But this `findlALl()` doesn't return a list by default.
        - So we need to override the method in `CustomerDAO` **interface** to get a list instead.
            - We `@Override` `List<Customer> findAll();`
- In `updateCustomer(int customerId, Customer customer)` method we don't have any update function directly.
    - But we have `save()` method. It works for both **insert** and **update** operation.
        - When we pass `Customer` and that doesn't contain the `id` then `save()` method works like insert method.
        - when we pass `Customer` that contains `CustomerId` then it will operate the update operation.
        - Here we pass `Customer` that contains `CustomerId` so it will operate like **update** operation..

- In `public void deleteCustomer(int customerId)` we delete the customer using `deleteById(customerId);` method.

---

---

## Fixing for Loosing Data on Start App Everytime

---

`resources/application.properties`

```properties
spring.datasource.url=jdbc:h2:~/test;DB_CLOSE_ON_EXIT=FALSE
spring.jpa.hibernate.ddl-auto=update
```

- First line indicate not to stop the database when we exit the server.
- Second line is to create the DB Table and next time force it to update not create again and again.

---

---

# **Custom Error Handling** [SOLVED]

---

- If I fetch the data from API which is not in DB?

## Exception

`exception/CustomerNotFoundException.java`

```java
package com.spring.cms.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message)
    {
        super(message);
    }
}
```
- This file handles an Exception

`exception/ApplicationError.java`

```java
package com.spring.cms.exception;

public class ApplicationError {

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```

- This file is common

## ApiController

`ApiController/ErrorHandler.java`

```java
package com.spring.cms.ApiController;

import com.spring.cms.exception.ApplicationError;
import com.spring.cms.exception.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {
    
    //Handle an Exception
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApplicationError> handleCustomerNotFoundException(CustomerNotFoundException exception, WebRequest webRequest)
    {
        ApplicationError error = new ApplicationError();
        error.setCode(101);
        error.setMessage(exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

- In this file all the exceptions will be handled.

## Service

`service/CustomerService.java`

```java
package com.spring.cms.service;

import com.spring.cms.dao.CustomerDAO;
import com.spring.cms.exception.CustomerNotFoundException;
import com.spring.cms.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    public Customer getCustomer(int customerId)
    {
        Optional<Customer> optionalCustomer =  customerDAO.findById(customerId);

        if(!optionalCustomer.isPresent())
        {
            throw new CustomerNotFoundException("Customer record is not available...");
        }

        return optionalCustomer.get();
    }
}
```

---

> For example We have another exception to handle. How to add another Exception?

## Exception

Create Below Exception in `exception` folder.

`exception/NotFoundException.java`

```java
package com.spring.cms.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message)
    {
        super(message);
    }
}
```

## ApiController

Add the Handler Method for the Exception in `ApiController`

`ApiController/ErrorHandler.java`

```java
package com.spring.cms.ApiController;

import com.spring.cms.exception.ApplicationError;
import com.spring.cms.exception.CustomerNotFoundException;
import com.spring.cms.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {

    // First Exception Handler
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApplicationError> handleCustomerNotFoundException(CustomerNotFoundException exception, WebRequest webRequest)
    {
        ApplicationError error = new ApplicationError();
        error.setCode(101);
        error.setMessage(exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Another Exception Handler
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApplicationError> handleNotFoundException(NotFoundException exception, WebRequest webRequest){
        ApplicationError error = new ApplicationError();
        error.setCode(201);
        error.setMessage(exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
```

- Look, We have added another `ResponseEntity` for new Exception in that `ErrorHandler` class.

## Service

And finally use this Exception where it needed.

`throw new NotFoundException("Data Not Found");`


---

---

# **Accessing Properties**

---

## resources

`resources/application.properties`

```properties
api_doc_url=http://api.localhost.com
```

- `url` is saved in that `api_doc_url` variable

## exception

`Exception/ApplicationError.java`

```java
package com.spring.cms.exception;

public class ApplicationError {

    private int code;
    private String message;
    private String details;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
```

- New private variable `details` is added with getter and setter

## ApiController

`ApiController/ErrorHandler.java`

```java
package com.spring.cms.ApiController;

import com.spring.cms.exception.ApplicationError;
import com.spring.cms.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @Value("${api_doc_url}")
    private String details;

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApplicationError> handleCustomerNotFoundException(CustomerNotFoundException exception, WebRequest webRequest)
    {
        ApplicationError error = new ApplicationError();
        error.setCode(101);
        error.setMessage(exception.getMessage());
        error.setDetails(details);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

- New variable defined as `private String details;`
- The variable annotated with `@Value("${api_doc_url}")` to access the `application.properties`
- Use this `error.setDetails(details);` to view in API.

---

# **Customizing a WhiteLabel Error Page**

---

## ApiController

`ApiController/Home.java`

```java
package com.spring.cms.ApiController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class Home {

    @GetMapping
    public String home(){
        return "Apllication is working!!! " + new Date();
    }

}
```

- Now the Error page has gone.

---

---

# **Adding a Filer**

---

- CNTL + I = Method Palate

`filter/MyFilter_1.java`

```java
package com.spring.cms.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
public class MyFilter_1 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 1 is called...");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
```

- If server got a response, before that response the Filter will be called.

---

---

## **Handing Multiple Filter**

---

`MyFilter_1.java`

```java
package com.spring.cms.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@Order(2)
public class MyFilter_1 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 1 is called...");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
```

- `@Order(2)`
    - This filter will be called 2nd

`MyFilter_2.java`

```java
package com.spring.cms.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@Order(3)
public class MyFilter_2 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 2 is called...");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
```

- `@Order(3)`
    - This filter will be called 3rd

`MyFilter_3.java`

```java
package com.spring.cms.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@Order(1)
public class MyFilter_3 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 3 is called...");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
```

- `@Order(1)`
    - This filter will be called 1st

---

---


