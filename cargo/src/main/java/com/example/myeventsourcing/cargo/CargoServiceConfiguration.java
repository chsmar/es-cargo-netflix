package com.example.myeventsourcing.cargo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Created by Administrador on 15/03/2016.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
public class CargoServiceConfiguration {
}
