package com.example.myeventsourcing.customer;

import com.example.myeventsourcing.customer.model.Customer;
import com.example.myeventsourcing.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.math.BigDecimal;

/**
 * Created by Administrador on 16/03/2016.
 */

@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
public class CustomerConfiguration implements CommandLineRunner {

    @Autowired
    private CustomerService customerService;

    @Override
    public void run(String... args) throws Exception {
        Customer c = new Customer();
        c.setName("pil");
        c.setBalance(new BigDecimal(2000));
        customerService.save(c);
    }
}
