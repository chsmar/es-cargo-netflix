package com.example.myeventsourcing.customer.service;

import com.example.myeventsourcing.common.event.cargo.CargoCreatedEvent;
import com.example.myeventsourcing.common.event.customer.CargoCustomerDebitFailedDueToInsufficientFoundsEvent;
import com.example.myeventsourcing.common.event.customer.CargoCustomerDebitedEvent;
import com.example.myeventsourcing.customer.model.Customer;
import com.example.myeventsourcing.customer.service.repo.CustomerRepository;
import com.example.myeventsourcing.event.Event;
import com.example.myeventsourcing.event.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrador on 11/03/2016.
 */

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Event<CargoCustomerDebitFailedDueToInsufficientFoundsEvent> debitFailedDueToInsufficientFoundsEventEvent;

    @Autowired
    private Event<CargoCustomerDebitedEvent> debitedEventEvent;

    @Listener
    public void onCreatedCargo(CargoCreatedEvent event) {
        Customer customer = customerRepository.findOne(event.getCustomerId());
        if(customer.getBalance().compareTo(event.getAmount()) < 0) {
            debitFailedDueToInsufficientFoundsEventEvent.fire(new CargoCustomerDebitFailedDueToInsufficientFoundsEvent());
        } else {
            customer.setBalance(customer.getBalance().subtract(event.getAmount()));
            customerRepository.save(customer);
            CargoCustomerDebitedEvent debitedEvent = new CargoCustomerDebitedEvent(event.getCustomerId(), event.getAmount());
            debitedEvent.setEntityId(customer.getId()+"");
            debitedEvent.setTxId(event.getTxId());
            debitedEventEvent.fire(debitedEvent);
        }
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public void save(Customer customer) {
        customerRepository.save(customer);
    }
}
