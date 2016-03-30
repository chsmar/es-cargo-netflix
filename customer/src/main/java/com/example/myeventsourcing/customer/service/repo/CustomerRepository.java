package com.example.myeventsourcing.customer.service.repo;

import com.example.myeventsourcing.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrador on 11/03/2016.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
