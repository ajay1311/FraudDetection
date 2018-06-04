package com.company.customer.service.resource;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.company.customer.service.mapper.CustomerMapper;
import com.company.customer.service.model.Customer;

@RestController
@RequestMapping("/v1/customers")
public class CustomerServiceController {

    private CustomerMapper customerMapper;

    public CustomerServiceController(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }


   /* @GetMapping("/allCustomers")
    public List<Customer> getAll() {
        return customerMapper.findAll();
    }

    @GetMapping("/update")
    private List<Customer> update() {

        Customer customer = new Customer();
        customer.setSsn(123456769);
        customer.setFirst_name("John");
        customer.setLast_name("Devis");
        customer.setDob("22/11/2011");
        customer.setGender(Gender.MALE);
        customerMapper.insert(customer);

        return customerMapper.findAll();
    }
    
   
*/
    
    
    @RequestMapping(value = "/getAllCustomers/", method = RequestMethod.GET)
    public ResponseEntity<List<Customer>> listAllCustomers() {
        List<Customer> customers = customerMapper.findAll();
        if (customers.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
    }
 
 
    @RequestMapping(value = "/getCustomer/{ssn}", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomer(@PathVariable("ssn") long ssn) {
        Customer customer = customerMapper.getCustomerBySSN(ssn);
        if (customer == null) {
            return new ResponseEntity(new CustomErrorType("Customer with ssn " + ssn + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }
 
 
    @RequestMapping(value = "/create/", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody Customer customer, UriComponentsBuilder ucBuilder) {
        if (customerMapper.getCustomerBySSN(customer.getSsn()) != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. Customer with ssn " + customer.getSsn() + " already exist."),HttpStatus.CONFLICT);
        }
        customerMapper.insert(customer);
 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/v1/customers/getCustomer/{ssn}").buildAndExpand(customer.getSsn()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
 
 
    @RequestMapping(value = "/update/{ssn}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("ssn") long ssn, @RequestBody Customer customer) {
 
        Customer currentCustomer = customerMapper.getCustomerBySSN(ssn);
 
        if (currentCustomer == null) {
            return new ResponseEntity(new CustomErrorType("Unable to upate. Customer with ssn " + ssn + " not found."),
                    HttpStatus.NOT_FOUND);
        }
 
        currentCustomer.setSsn(customer.getSsn());
        currentCustomer.setFirst_name(customer.getFirst_name());
        currentCustomer.setLast_name(customer.getLast_name());
        currentCustomer.setDob(customer.getDob());
        currentCustomer.setGender(customer.getGender());
 
        customerMapper.updateBySSN(currentCustomer);
        return new ResponseEntity<Customer>(currentCustomer, HttpStatus.OK);
    }
 
 
    @RequestMapping(value = "/delete/{ssn}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("ssn") long ssn) {
 
        Customer customer = customerMapper.getCustomerBySSN(ssn);
        if (customer == null) {
            return new ResponseEntity(new CustomErrorType("Unable to delete. Customer with ssn " + ssn + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        customerMapper.deleteBySSN(ssn);
        return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
    }
 
}
