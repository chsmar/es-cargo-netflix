package com.example.myeventsourcing.customer.controller;

import com.example.myeventsourcing.customer.model.Customer;
import com.example.myeventsourcing.customer.service.CustomerService;
import com.example.myeventsourcing.customer.service.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Administrador on 16/03/2016.
 */

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> getCustomers() {
        return customerService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void saveCustomer(@RequestBody Customer customer) {
        customerService.save(customer);
    }
}
