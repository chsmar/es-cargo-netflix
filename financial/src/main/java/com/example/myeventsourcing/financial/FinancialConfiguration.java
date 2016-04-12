package com.example.myeventsourcing.financial;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Created by Administrador on 16/03/2016.
 */

@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
public class FinancialConfiguration {
}
